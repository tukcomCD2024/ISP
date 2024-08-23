package com.project.how.data_class.dto.recode.receipt

import com.google.gson.annotations.SerializedName

typealias ReceiptSimpleList = List<ReceiptListItem>

data class ReceiptListItem(
    @SerializedName("scheduleId")
    val id : Long,
    @SerializedName("scheduleName")
    val scheduleName : String?,
    @SerializedName("startDate")
    val startDate : String,
    @SerializedName("endDate")
    val endDate : String,
    @SerializedName("currencyName")
    val currency : String,
    @SerializedName("totalReceiptsPrice")
    val totalPrice : Double,
    @SerializedName("receiptCount")
    val count : Long
)
