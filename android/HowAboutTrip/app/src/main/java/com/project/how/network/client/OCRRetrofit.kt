package com.project.how.network.client

import com.google.gson.GsonBuilder
import com.project.how.BuildConfig
import com.project.how.network.api_interface.OCRService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object OCRRetrofit {
    private const val BASE_URL = BuildConfig.OCR_SERVER_URL

    fun getApiService() : OCRService? = getInstance()
        ?.create(OCRService::class.java)

    private fun getInstance() : Retrofit? {
        val gson = GsonBuilder().setLenient().create()

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        httpClient.addInterceptor(loggingInterceptor)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }
}