package com.zzz.oss_rubbishcommunity.manager.di


import com.zzz.oss_rubbishcommunity.BuildConfig
import com.zzz.oss_rubbishcommunity.manager.api.ImageService
import com.zzz.oss_rubbishcommunity.manager.api.MachineService
import com.zzz.oss_rubbishcommunity.manager.base.ApiClient
import com.zzz.oss_rubbishcommunity.manager.base.HeaderInterceptor
import com.zzz.oss_rubbishcommunity.manager.base.NetErrorInterceptor
import com.zzz.oss_rubbishcommunity.manager.api.NewsService
import com.zzz.oss_rubbishcommunity.manager.api.UserService
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.concurrent.TimeUnit

val apiModule = Kodein.Module("api") {

    //api
    bind<ApiClient>() with singleton { provideApiClient() }

    bind<NewsService>() with singleton { instance<ApiClient>().createService(NewsService::class.java) }
    bind<UserService>() with singleton { instance<ApiClient>().createService(UserService::class.java) }
    bind<ImageService>() with singleton { instance<ApiClient>().createService(ImageService::class.java) }
    bind<MachineService>() with singleton { instance<ApiClient>().createService(MachineService::class.java) }

/*    bind<BaiDuIdentifyService>() with singleton {
        instance<BaiduIdentifyClient>().createService(
            BaiDuIdentifyService::class.java
        )
    }*/

}

/*fun databaseModule(app: Application) = Kodein.Module("database") {
    bind<AppDatabase>() with singleton {
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME_APP
        ).addCallback(AppDatabase.CALLBACK)
            .build()
    }

    bind<ArticleSearchHistoryDao>() with provider { instance<AppDatabase>().articleSearchHistoryDao() }

}*/


fun provideApiClient(): ApiClient {
    val client = ApiClient.Builder()
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    client.okBuilder
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(NetErrorInterceptor())
/*        .addInterceptor(AddCookiesInterceptor())
        .addInterceptor(ReceivedCookiesInterceptor())*/
        .readTimeout(30, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }

    return client.build(baseUrl = BuildConfig.BASE_URL)
}

