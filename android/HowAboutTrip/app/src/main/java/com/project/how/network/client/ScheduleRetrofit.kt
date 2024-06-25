package com.project.how.network.client

import com.google.gson.GsonBuilder
import com.project.how.BuildConfig
import com.project.how.network.api_interface.MemberService
import com.project.how.network.api_interface.ScheduleService
import com.project.how.network.converter_factory.NullOnEmptyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ScheduleRetrofit {
    private const val BASE_URL = BuildConfig.API_SERVER

    fun getApiService() : ScheduleService? = getInstance()?.create(ScheduleService::class.java)

    private fun getInstance() : Retrofit? {
        val gson = GsonBuilder().setLenient().create()

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)

        val loggingInterceptor =  HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        httpClient.addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }
}