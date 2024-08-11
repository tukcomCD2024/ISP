package com.project.how.network.api_interface

import com.project.how.data_class.dto.recode.ocr.OcrResponse
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OCRService {
    @Multipart
    @POST("receipt/v1/verbose/file")
    @Headers("accept: application/json")
    fun uploadReceipt(
        @Part file: MultipartBody.Part,
        @Part("extractLineItems") detailCheck : RequestBody,
        @Header("apikey") apiKey: String,
    ): Call<OcrResponse>
}