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
    var oldNesList = emptyList<NewsResultModel.News>()
    val newsList = MutableLiveData(emptyList<NewsResultModel.News>())
    val searchKey = MutableLiveData("")


    val isRefreshing = MutableLiveData(false)
    private var syncKey: Long? = 0

    fun fetchNews(isRefresh:Boolean = false) {
        newsService.fetchNews(
                syncKey = syncKey
        ).dealRefresh()
                .doOnApiSuccess {
                    val resultList = it.data?.newsDetailList
                    if (resultList?.isNotEmpty() == true) {
                        syncKey = if(isRefresh) 0 else it.data.newsDetailList.minBy { it.newsId?:0 }?.newsId
                        newsList.postValue(it.data.newsDetailList)
                        oldNesList = resultList
                    }
                }
    }

    private fun <T> Single<T>.dealRefresh() =
            doOnSubscribe { isRefreshing.postValue(true) }
                    .doFinally { isRefreshing.postValue(false) }


    fun deleteNews(newsId: Long?) {
        newsService.deleteNews(newsId).doOnApiSuccess {
            fetchNews()
        }
    }


}