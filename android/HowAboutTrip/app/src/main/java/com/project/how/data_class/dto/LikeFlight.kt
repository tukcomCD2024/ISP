package com.project.how.data_class.dto

typealias LikeFlightList = List<LikeFlightElement>

data class LikeFlightElement (
    val carrierCode: String,
    val totalPrice: Long,
    val abroadDuration: String,
    val abroadDepartureTime: String,
    val abroadArrivalTime: String,
    val homeDuration: String,
    val homeDepartureTime: String,
    val homeArrivalTime: String,
    val departureIataCode: String,
    val arrivalIataCode: String,
    val nonstop: Boolean,
    val transferCount: Long
)
