package com.zzz.oss_rubbishcommunity.util


import com.luck.picture.lib.entity.LocalMedia
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UpProgressHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import com.zzz.oss_rubbishcommunity.MainApplication
import com.zzz.oss_rubbishcommunity.manager.api.ImageService
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import com.zzz.oss_rubbishcommunity.model.image.GetQiNiuTokenRequestModel
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//七牛云工具
class QiNiuUtil

//上传单张图片
fun ImageService.upLoadImage(
        imagePath: String,
        onUpdateProgress: (Int) -> Unit
): Single<String> {
    val upKey = "${SharedPrefModel.userEmail}/${System.currentTimeMillis()}"
    return Single.create { emitter ->
        getQiNiuToken(GetQiNiuTokenRequestModel("dew", listOf(upKey)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(dealQiNiuErrorCode())
                .doOnSuccess { tokenRsp ->
                    tokenRsp.data?.map {
                        UploadManager().put(
                                imagePath,
                                upKey,
                                it.value,
                                { key, responseInfo, response ->
                                    //上传成功，返回结果
                                    emitter.onSuccess(key)
                                }, UploadOptions(
                                null,
                                "test-type",
                                true,
                                UpProgressHandler { _, percent ->
                                    //上传中，返回进度
                                    onUpdateProgress((percent * 100).toInt())
                                }, null
                        )
                        )
                    }
                }.compose(dealQiNiuErrorCode())
                .doOnError {
                    MainApplication.showToast("${it.message}")
                }
                .subscribe()
    }
}


//上传图片列表
fun ImageService.upLoadImageList(
        imagePathList: List<String>,
        onUpdateProgress: ((Int) -> Unit)? = null
): Single<List<String>> {
    val pathList = mutableListOf<String>()
    val upKeyList = mutableListOf<String>()
    var mill = System.currentTimeMillis()
    imagePathList.map { mediaPath ->
        pathList.add(mediaPath)
        mill += 100
        upKeyList.add("${SharedPrefModel.userEmail}/$mill")
    }
    return Single.create { emitter ->
        getQiNiuToken(GetQiNiuTokenRequestModel("dew", upKeyList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { tokenRsp ->
                    val resultKeyList = mutableListOf<String>()
                    tokenRsp.data?.map { entry ->
                        //key是图片在七牛云的上的相对path，value是上传所需token
                        val index = upKeyList.indexOf(entry.key)
                        UploadManager().put(
                                pathList[index], upKeyList[index], entry.value,
                                { key, _, _ ->
                                    if (index == imagePathList.size - 1) {
                                        //已经全部上传成功，返回结果
                                        emitter.onSuccess(upKeyList)
                                    }
                                }, UploadOptions(
                                null, "test-type", true,
                                UpProgressHandler { _, percent ->
                                    //上传中，返回进度
                                    onUpdateProgress?.invoke(((100 / (pathList.size)) * percent).toInt())
                                }, null
                        )
                        )
                    }
                }
                .compose(dealQiNiuErrorCode())
                .doOnError {
                    MainApplication.showToast("${it.message}")
                }
                .subscribe()
    }
}


data class QiNiuUpLoadError(val responseInfo: ResponseInfo) : Throwable()

fun <T> dealQiNiuErrorCode(): SingleTransformer<T, T> {
    return SingleTransformer { obs ->
        obs.doOnSuccess { responseInfo ->
            if (responseInfo is ResponseInfo) {
                when (responseInfo) {
                    responseInfo.isOK -> {
                        //上传成功
                        return@doOnSuccess
                    }
                    !responseInfo.isOK -> {
                        //上传失败，抛出异常
                        throw QiNiuUpLoadError(responseInfo)
                    }
                    else -> {
                        //其他
                        throw UnknownError()
                    }
                }
            }
        }
    }
}


