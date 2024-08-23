package com.project.how.data_class.dto.recode.receipt

import com.google.gson.annotations.SerializedName

data class GetReceiptListResponse(
    @SerializedName("scheduleName")
    val scheduleName: String,
    @SerializedName("startDate")
    val startDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("currencyName")
    val currencyName: String,
    @SerializedName("totalReceiptsPrice")
    val totalReceiptsPrice: Double,
    @SerializedName("receiptList")
    val receiptList: List<ReceiptList>
)

data class ReceiptList (
    @SerializedName("receiptId")
    val receiptId: Long,
    @SerializedName("storeName")
    val storeName: String,
    @SerializedName("storeType")
    val storeType: StoreType,
    @SerializedName("price")
    val price: Double,
    @SerializedName("orderNum")
    val orderNum: Long,
    @SerializedName("purchaseDate")
    val purchaseDate: String
)

enum class StoreType {
    HOTEL,
    PLACE,
    AIRPLANE
}
