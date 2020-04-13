package com.zzz.oss_rubbishcommunity.ui.fragment.newsdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zzz.oss_rubbishcommunity.manager.api.NewsService
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseViewModel
import org.kodein.di.generic.instance


class NewsDetailViewModel(application: Application) : BaseViewModel(application) {
	val news = MutableLiveData<NewsResultModel.News>()
	val newsService by instance<NewsService>()

	fun deleteNews(){

	}
}