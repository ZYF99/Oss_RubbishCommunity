package com.zzz.oss_rubbishcommunity.ui.fragment.news

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zzz.oss_rubbishcommunity.manager.api.ImageService
import com.zzz.oss_rubbishcommunity.manager.api.NewsService
import com.zzz.oss_rubbishcommunity.model.api.news.AddNewsRequestModel
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseViewModel
import com.zzz.oss_rubbishcommunity.util.upLoadImageList
import io.reactivex.Single
import org.kodein.di.generic.instance

class NewsViewModel(application: Application) : BaseViewModel(application) {

    private val newsService by instance<NewsService>()
    private val imageService by instance<ImageService>()
    val newsList = MutableLiveData(emptyList<NewsResultModel.News>())
    val isRefreshing = MutableLiveData(false)
    private var syncKey: Long? = 0

    fun fetchNews() {
        newsService.fetchNews(
                syncKey = syncKey
        ).dealRefresh()
                .doOnApiSuccess {
                    if (it.data?.newsDetailList?.isNotEmpty() == true) {
                        syncKey = it.data.newsDetailList.minBy { it.newsId }?.newsId
                        newsList.postValue(it.data.newsDetailList)
                    }
                }
    }

    fun addNews(
            title: String,
            type: Int,
            content: String,
            imagePathList: List<String>,
            needTop: Int
    ) {
        imageService.upLoadImageList(imagePathList) { }
                .flatMap { imagePathList ->
                    newsService.addNews(
                            AddNewsRequestModel(
                                    title,
                                    type,
                                    content,
                                    imagePathList,
                                    needTop
                            )
                    )
                }.doOnApiSuccess {
                    fetchNews()
                }
    }

    private fun <T> Single<T>.dealRefresh() =
            doOnSubscribe { isRefreshing.postValue(true) }
                    .doFinally { isRefreshing.postValue(false) }


/*    fun editNews(
            id: Long,
            originImageUrl: String,
            title: String,
            content: String,
            imageFile: File?,
            needTop: Int
    ) {
        fun edit(imagePath: String) = newsService.editNews(
                EditNewsRequestModel(
                        id,
                        title,
                        content,
                        imagePath,
                        needTop
                )
        )

        val single = if (imageFile != null) {
            userService.upLoadImage(imageFile)
                    ?.flatMap {
                        edit(it.data?.imagePath ?: "")
                    }
        } else edit(originImageUrl)
        single?.doOnApiSuccess {
            fetchNews()
        }

    }

    fun deleteNews(newsId: Long) {
        newsService.deleteNews(newsId).doOnApiSuccess {
            fetchNews()
        }
    }*/


}