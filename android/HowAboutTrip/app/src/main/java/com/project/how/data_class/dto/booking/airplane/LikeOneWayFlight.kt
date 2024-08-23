package com.project.how.data_class.dto.booking.airplane

import com.google.gson.annotations.SerializedName

typealias LikeOneWayFlight = List<LikeOneWayFlightElement>

data class LikeOneWayFlightElement (
    @SerializedName("carrierCode")
    val carrierCode: String,
    @SerializedName("totalPrice")
    val totalPrice: Long,
    @SerializedName("departureIataCode")
    val departureIataCode: String,
    @SerializedName("arrivalIataCode")
    val arrivalIataCode: String,
    @SerializedName("abroadDuration")
    val abroadDuration: String,
    @SerializedName("abroadDepartureTime")
    val abroadDepartureTime: String,
    @SerializedName("abroadArrivalTime")
    val abroadArrivalTime: String,
    @SerializedName("nonstop")
    val nonstop: Boolean,
    @SerializedName("transferCount")
    val transferCount: Long,
    @SerializedName("adult")
    val adult : Long,
    @SerializedName("children")
    val children : Long
)
