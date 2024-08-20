package com.project.how.network.api_interface

import com.project.how.data_class.dto.EmptyResponse
import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse
import com.project.how.data_class.dto.recode.receipt.GetReceiptDetail
import com.project.how.data_class.dto.recode.receipt.ReceiptSimpleList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("receipts/schedules/list")
    fun getScheduleListWithReceipt() : Call<ReceiptSimpleList>

    @DELETE("receipts/{receiptId}")
    fun deleteReceipt(
        @Path("receiptId", encoded = true) id : Long
    ) : Call<EmptyResponse>

    @GET("receipts/detail/{receiptId}/list")
    fun getReceiptDetail(
        @Path("receiptId", encoded = true) id : Long
    ) : Call<GetReceiptDetail>

    @Multipart
    @PUT("receipts/{receiptId}")
    fun updateReceipt(
        @Path("receiptId", encoded = true) id : Long,
        @Part("request") detail : RequestBody,
        @Part receiptImg : MultipartBody.Part
    ) : Call<String>

}