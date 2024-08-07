package com.project.how.data_class.dto.booking.airplane

import com.google.gson.annotations.SerializedName

typealias GetLikeFlightResponse = List<GetLikeFlightResponseElement>

data class GetLikeFlightResponseElement(
    @SerializedName("id")
    val id: Long,
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
    val homeDuration: String? = null,
    @SerializedName("homeDepartureTime")
    val homeDepartureTime: String? = null,
    @SerializedName("homeArrivalTime")
    val homeArrivalTime: String? = null,
    @SerializedName("departureIataCode")
    val departureIataCode: String,
    @SerializedName("arrivalIataCode")
    val arrivalIataCode: String,
    @SerializedName("transferCount")
    val transferCount: String,
    @SerializedName("adult")
    val adult : Long,
    @SerializedName("children")
    val children : Long
)
