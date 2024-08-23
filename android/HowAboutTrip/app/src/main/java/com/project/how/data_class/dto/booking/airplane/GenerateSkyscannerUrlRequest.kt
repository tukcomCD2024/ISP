package com.project.how.data_class.dto.booking.airplane

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
    @SerializedName("adult")
    val adult : Long,
    @SerializedName("children")
    val children : Long,
    @SerializedName("departureTime")
    val departureTime: String,
    @SerializedName("transferCount")
    val transferCount : Long
)

data class GenerateOneWaySkyscannerUrlRequest(
    @SerializedName("departureIataCode")
    val departureiataCode : String,
    @SerializedName("arrivalIataCode")
    val arrivalIataCode : String,
    @SerializedName("departureDate")
    val departureDate : String,
    @SerializedName("adult")
    val adult : Long,
    @SerializedName("children")
    val children : Long,
    @SerializedName("departureTime")
    val departureTime: String,
    @SerializedName("transferCount")
    val transferCount : Long
)
