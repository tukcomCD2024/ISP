package com.project.how.data_class.dto

import java.io.Serializable

typealias GetFlightOffersResponse = List<GetFlightOffersResponseElement>
typealias GetOneWayFlightOffersResponse = List<GetOneWayFlightOffersResponseElement>

data class RoundTripFlightOffers(
    val data : ArrayList<GetFlightOffersResponseElement>
) : Serializable

data class OneWayFlightOffers(
    val data : ArrayList<GetOneWayFlightOffersResponseElement>
) : Serializable

data class GetFlightOffersResponseElement (
    val id: String,
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
): Serializable

data class GetOneWayFlightOffersResponseElement(
    val id: String,
    val carrierCode: String,
    val totalPrice: Long,
    val departureIataCode: String,
    val arrivalIataCode: String,
    val nonstop: Boolean,
    val transferCount: Long,
    val abroadDuration: String,
    val abroadDepartureTime: String,
    val abroadArrivalTime: String
) : Serializable
