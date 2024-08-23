package com.project.how.data_class.dto.recode.receipt

import com.google.gson.annotations.SerializedName

data class GetReceiptDetail(
    @SerializedName("receiptId")
    val id : Long,
    @SerializedName("purchaseDate")
    val purchaseDate : String,
    @SerializedName("orderNum")
    val orderNum : Long,
    @SerializedName("receiptImg")
    val image : String,
    @SerializedName("totalPrice")
    val totalPrice : Double,
    @SerializedName("receiptDetailList")
    val receiptDetailList : List<ReceiptDetailListItem>
)
