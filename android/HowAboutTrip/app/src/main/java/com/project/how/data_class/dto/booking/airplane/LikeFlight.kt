package com.project.how.data_class.dto.booking.airplane

import com.google.gson.annotations.SerializedName

typealias LikeFlight = List<LikeFlightElement>

data class LikeFlightElement (
    @SerializedName("carrierCode")
    val carrierCode: String,
    @SerializedName("totalPrice")
    val totalPrice: Long,
    @SerializedName("abroadDuration")
    val abroadDuration: String,
    @SerializedName("abroadDepartureTime")
    val abroadDepartureTime: String,
    @SerializedName("abroadArrivalTime")
    val abroadArrivalTime: String,
    @SerializedName("homeDuration")
    val homeDuration: String,
    @SerializedName("homeDepartureTime")
    val homeDepartureTime: String,
    @SerializedName("homeArrivalTime")
    val homeArrivalTime: String,
    @SerializedName("departureIataCode")
    val departureIataCode: String,
    @SerializedName("arrivalIataCode")
    val arrivalIataCode: String,
    @SerializedName("nonstop")
    val nonstop: Boolean,
    @SerializedName("transferCount")
    val transferCount: Long,
    @SerializedName("adult")
    val adult : Long,
    @SerializedName("children")
    val children : Long
)
