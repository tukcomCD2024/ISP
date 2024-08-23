package com.project.how.data_class.dto.recode.receipt

import com.google.gson.annotations.SerializedName

data class ReceiptDetail(
    @SerializedName("scheduleId")
    val id : Long,
    @SerializedName("storeName")
    var storeName : String?,
    @SerializedName("storeType")
    val storeType: StoreType,
    @SerializedName("purchaseDate")
    val purchaseDate : String,
    @SerializedName("totalPrice")
    var totalPrice : Double,
    @SerializedName("receiptDetails")
    var receiptDetails : List<ReceiptDetailListItem>
)

data class ReceiptDetailListItem(
    @SerializedName("item")
    var title: String,
    @SerializedName("count")
    var count : Long,
    @SerializedName("itemPrice")
    var itemPrice : Double
)
