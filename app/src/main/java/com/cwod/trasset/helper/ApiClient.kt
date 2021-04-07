package com.cwod.trasset.helper

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiClient {

    lateinit var retroClientCache: Retrofit
    var retroClient: Retrofit
    private const val BASE_URL = Urls.BASE_URL

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES) // write timeout
            .readTimeout(2, TimeUnit.MINUTES) // read timeout
            .addInterceptor(interceptor)
            .build()
        retroClient = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient).build()
    }


    fun instantiateWithAccessToken(context: Context, accessToken: String?): ApiClient {
        val cache = Cache(context.cacheDir, 5 * 1024 * 1024)     //5 MB
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().cache(cache)
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES) // write timeout
            .readTimeout(2, TimeUnit.MINUTES) // read timeout
            .addInterceptor(interceptor)
            .addInterceptor(ChuckerInterceptor(context))
            .addInterceptor(object : Interceptor { // Adding Permanent Header
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original: Request = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()
                        .header("Authorization", "Token $accessToken")
//                        .header("Cache-Control", "public, only-if-cached, max-stale=${60 * 60 * 24 * 30}")
                    val request: Request = requestBuilder.build()
                    return chain.proceed(request)
                }
            }).build()
        retroClientCache = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient).build()
        return this


    }

}