package com.zzz.oss_rubbishcommunity.manager.api

import com.zzz.oss_rubbishcommunity.model.api.ResultModel
import com.zzz.oss_rubbishcommunity.model.image.GetQiNiuTokenRequestModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


interface ImageService {

    //获取七牛云上传图片凭证
    @POST("api/common/image/token")
    fun getQiNiuToken(@Body requestModel: GetQiNiuTokenRequestModel): Single<ResultModel<Map<String, String>>>


}