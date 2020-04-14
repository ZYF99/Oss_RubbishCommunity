package com.zzz.oss_rubbishcommunity.manager.api

import com.zzz.oss_rubbishcommunity.model.api.ResultModel
import com.zzz.oss_rubbishcommunity.model.api.news.AddNewsRequestModel
import com.zzz.oss_rubbishcommunity.model.api.news.EditNewsRequestModel
import com.zzz.oss_rubbishcommunity.model.api.news.NewsResultModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface NewsService {

    //获取资讯
    @GET("api/news")
    fun fetchNews(
            @Query("pageSize")pageSize:Int = 10,
            @Query("syncKey")syncKey:Long?
    ): Single<ResultModel<NewsResultModel>>

    //添加新闻
    @POST("api/news")
    fun addNews(@Body addNewsRequestModel: AddNewsRequestModel): Single<ResponseBody>

    //修改新闻
    @PUT("api/news")
    fun editNews(@Body editNewsRequestModel: EditNewsRequestModel): Single<ResponseBody>

    //删除新闻
    @DELETE("api/news")
    fun deleteNews(@Query ("newsId")newsId:Long?): Single<ResponseBody>

/*    //获取新闻列表
    @GET("news")
    fun getNews(): Single<ResultModel<NewsListModel>>

    //添加新闻
    @POST("news")
    fun addNews(@Body addNewsRequestModel: AddNewsRequestModel): Single<ResponseBody>

    //修改新闻
    @PUT("news")
    fun editNews(@Body editNewsRequestModel: EditNewsRequestModel): Single<ResponseBody>

    //删除新闻
    @DELETE("news")
    fun deleteNews(@Query ("newsId")newsId:Long): Single<ResponseBody>*/


}