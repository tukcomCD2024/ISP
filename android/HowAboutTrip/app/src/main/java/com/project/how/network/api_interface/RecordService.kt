package com.project.how.network.api_interface

import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse
import com.project.how.data_class.dto.recode.receipt.ReceiptDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RecordService {
    @GET("receipts/{scheduleId}/list")
    fun getReceiptList(
        @Path("scheduleId", encoded = true) scheduleId : Long
    ) : Call<GetReceiptListResponse>

    @Multipart
    @POST("receipts")
    fun saveReceipt(
        @Part("request") detail : RequestBody,
        @Part receiptImg : MultipartBody.Part
    ) : Call<String>
}