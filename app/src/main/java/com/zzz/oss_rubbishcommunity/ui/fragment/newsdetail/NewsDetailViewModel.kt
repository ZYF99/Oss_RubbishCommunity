package com.zzz.oss_rubbishcommunity.ui.fragment.newsdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zzz.oss_rubbishcommunity.manager.api.ImageService
import com.zzz.oss_rubbishcommunity.manager.api.NewsService
import com.zzz.oss_rubbishcommunity.model.api.news.EditNewsRequestModel
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseViewModel
import com.zzz.oss_rubbishcommunity.util.upLoadImageList
import org.kodein.di.generic.instance


class NewsDetailViewModel(application: Application) : BaseViewModel(application) {
    val news = MutableLiveData<NewsResultModel.News>()
    val newsService by instance<NewsService>()
    val imageService by instance<ImageService>()
    val isEdit = MutableLiveData(false)

    fun deleteNews(action: (() -> Unit)?) {
        newsService.deleteNews(news.value?.newsId).doOnApiSuccess {
            action?.invoke()
        }
    }

    fun editNews() {
        fun edit(imageList: List<String> = emptyList()) = newsService.editNews(
                EditNewsRequestModel(
                        category = news.value?.category,
                        frontCoverImages = imageList,
                        newsId = news.value?.newsId,
                        newsType = news.value?.newsType,
                        payload = news.value?.payload,
                        title = news.value?.title
                )
        )
        val single = if (news.value?.frontCoverImages?.isNotEmpty() == true) {
            imageService.upLoadImageList(news.value?.frontCoverImages ?: emptyList())
                    .flatMap {
                        edit(it)
                    }
        } else edit()
        single.doOnApiSuccess {

        }
    }
}