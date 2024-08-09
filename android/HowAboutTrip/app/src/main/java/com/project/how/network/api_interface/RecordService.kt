package com.project.how.network.api_interface

import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Path

interface RecordService {
    @GET("receipts/{scheduleId}/list")
    fun getReceiptList(
        @Path("scheduleId", encoded = true) scheduleId : Long
    ) : Call<GetReceiptListResponse>
}