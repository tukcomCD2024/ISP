package com.project.how.data_class.dto

typealias LikeOneWayFlight = List<LikeOneWayFlightElement>

data class LikeOneWayFlightElement (
    val carrierCode: String,
    val totalPrice: Long,
    val departureIataCode: String,
    val arrivalIataCode: String,
    val abroadDuration: String,
    val abroadDepartureTime: String,
    val abroadArrivalTime: String,
    val nonstop: Boolean,
    val transferCount: Long
)
