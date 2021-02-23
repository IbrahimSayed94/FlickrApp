package com.example.flickrapp.network

import com.example.flickrapp.utils.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var servicesApiInterface:ApiService?=null

    fun build(): ApiService {


        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        httpClient.
        addInterceptor(interceptor())
            .addInterceptor(HeaderInterceptor())
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)



        val retrofit: Retrofit = builder.client(httpClient.build()).build()


        servicesApiInterface = retrofit.create(
            ApiService::class.java)

        return servicesApiInterface as ApiService
    }

    private fun interceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Accept-Language", "")
                    .addHeader("Authorization", "")
                    .build()
            )
        }
    }
}