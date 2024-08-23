package com.project.how.network.client

import com.google.gson.GsonBuilder
import com.project.how.Application
import com.project.how.BuildConfig
import com.project.how.network.api_interface.MemberService
import com.project.how.network.client.interceptor.AuthInterceptor
import com.project.how.network.converter_factory.NullOnEmptyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object MemberRetrofit {
    private const val BASE_URL = BuildConfig.API_SERVER

    fun getApiService() : MemberService? = getInstance()?.create(MemberService::class.java)

    private fun getInstance() : Retrofit? {
        val gson = GsonBuilder().setLenient().create()

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

        val loggingInterceptor =  HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        httpClient.addInterceptor(loggingInterceptor)
        httpClient.addInterceptor(AuthInterceptor(Application.applicationContext()))

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }
}