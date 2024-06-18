package com.project.how.data_class.dto

typealias GetLikeFlightResponse = List<GetLikeFlightResponseElement>

data class GetLikeFlightResponseElement(
    val id: Long,
    val carrierCode: String,
    val totalPrice: Long,
    val abroadDuration: String,
    val abroadDepartureTime: String,
    val abroadArrivalTime: String,
    val homeDuration: String? = null,
    val homeDepartureTime: String? = null,
    val homeArrivalTime: String? = null,
    val departureIataCode: String,
    val arrivalIataCode: String,
    val transferCount: String
)
