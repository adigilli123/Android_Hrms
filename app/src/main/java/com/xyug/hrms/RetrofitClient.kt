//package com.xyug.hrms
//
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitClient {
//    private const val home_URL = "http://stg-hrms-saas-client-api.xyug.in:3009/"
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(home_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//    fun get(): Retrofit = retrofit
//}
package com.xyug.hrms

import com.airbnb.lottie.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

  object RetrofitClient {
    private const val HOME_URL = "http://192.168.168.164:3009/"

    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging).connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(HOME_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build()
    }
    fun get(): Retrofit = retrofit
}
