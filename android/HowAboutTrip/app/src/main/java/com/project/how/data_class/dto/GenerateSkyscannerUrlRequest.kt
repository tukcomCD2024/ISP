package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

data class GenerateSkyscannerUrlRequest(
    @SerializedName("departureIataCode")
    val departureiataCode : String,
    @SerializedName("arrivalIataCode")
    val arrivalIataCode : String,
    @SerializedName("departureDate")
    val departureDate : String,
    @SerializedName("returnDate")
    val returnDate : String,
    @SerializedName("adults")
    val adults : Long,
    @SerializedName("children")
    val children : Long,
    @SerializedName("departureTime")
    val departureTime: String,
    @SerializedName("transferCount")
    val transferCount : Long
)
