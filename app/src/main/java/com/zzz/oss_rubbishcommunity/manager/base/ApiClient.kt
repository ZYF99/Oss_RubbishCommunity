package com.zzz.oss_rubbishcommunity.manager.base

import android.annotation.SuppressLint
import com.zzz.oss_rubbishcommunity.BuildConfig
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.reflect.KClass


class ApiClient constructor(val retrofit: Retrofit, val okHttpClient: OkHttpClient) {

    fun <S> createService(serviceClass: Class<S>): S = retrofit.create(serviceClass)

    fun <S : Any> createService(serviceClass: KClass<S>): S = createService(serviceClass.java)

    class Builder(
        val apiAuthorizations: MutableMap<String, Interceptor> = linkedMapOf(),
        val okBuilder: OkHttpClient.Builder = OkHttpClient.Builder(),
        val adapterBuilder: Retrofit.Builder = Retrofit.Builder()
    ) {

        private val allowAllSSLSocketFactory: Pair<SSLSocketFactory, X509TrustManager>?
            get() {
                return try {
                    val sslContext = SSLContext.getInstance("TLS")
                    val trustManager = trustManagerAllowAllCerts
                    sslContext.init(
                        null,
                        arrayOf<TrustManager>(trustManager),
                        SecureRandom()
                    )

                    sslContext.socketFactory to trustManager
                } catch (e: Exception) {
                    Timber.e(e, "allowAllSSLSocketFactory has error")
                    null
                }
            }

        private val trustManagerAllowAllCerts: X509TrustManager
            get() = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>, authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>, authType: String
                ) {
                }
            }

        fun setAllowAllCerTificates(): Builder {
            allowAllSSLSocketFactory?.apply {
                okBuilder.sslSocketFactory(first, second)
                okBuilder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            }

            return this
        }

        fun build(baseUrl: String = BuildConfig.BASE_URL): ApiClient {
            adapterBuilder
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(globalMoshi).asLenient())

            val client = okBuilder.callTimeout(600,TimeUnit.SECONDS)
                .connectTimeout(600,TimeUnit.SECONDS)
                .addInterceptor { chain ->
                val origin = chain.request()
                val request = origin
                    .newBuilder()
                    .header("Accept", "application/json;charset=UTF-8")
                    .header("X-Token", SharedPrefModel.token)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(origin.method(), origin.body())
                    .build()
                chain.proceed(request)
            }.build()
            if (BuildConfig.ALLOW_ALL_CERTIFICATES)
                setAllowAllCerTificates()
            val retrofit = adapterBuilder.client(client).build()
            return ApiClient(retrofit, client)
        }
    }

    companion object {
        val defaultClient: ApiClient
            get() = Builder().build()
    }
}
