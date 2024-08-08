package com.project.how.data_class.dto.ocr

import com.google.gson.annotations.SerializedName

data class OcrResponse (
    @SerializedName("totalAmount")
    val totalAmount: Amount,
    @SerializedName("date")
    val date: Date,
    @SerializedName("entities")
    val entities: Entities,
    @SerializedName("merchantName")
    val merchantName: Date,
)

data class Date (
    @SerializedName("data")
    val data: String,
)


data class Entities (
    @SerializedName("productLineItems")
    val productLineItems: List<ProductLineItem>,
)

data class ProductLineItem (
    @SerializedName("data")
    val data: ProductLineItemData,
)

data class ProductLineItemData (
    @SerializedName("unitPrice")
    val unitPrice: Quantity? = null,
    @SerializedName("totalPrice")
    val totalPrice: Quantity? = null,
    @SerializedName("name")
    val name: Name,
)

data class Name (
    @SerializedName("data")
    val data: String,
    @SerializedName("text")
    val text: String
)

data class Quantity (
    @SerializedName("data")
    val data: Long,
    @SerializedName("text")
    val text: String
)

data class Amount (
    @SerializedName("data")
    val data: Double,
    @SerializedName("text")
    val text: String,
    @SerializedName("currencyCode")
    val currencyCode: String,
)

