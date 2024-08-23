package com.project.how.network.client

import com.google.gson.GsonBuilder
import com.project.how.Application
import com.project.how.BuildConfig
import com.project.how.network.api_interface.RecordService
import com.project.how.network.client.interceptor.AuthInterceptor
import com.project.how.network.converter_factory.NullOnEmptyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RecordRetrofit {
    private const val BASE_URL = BuildConfig.API_SERVER

    fun getApiService() : RecordService? = getInstance()
        ?.create(RecordService::class.java)

    private fun getInstance() : Retrofit? {
        val gson = GsonBuilder().setLenient().create()

        val httpClient = OkHttpClient.Builder()

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