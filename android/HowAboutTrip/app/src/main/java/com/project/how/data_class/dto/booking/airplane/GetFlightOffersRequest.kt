package com.project.how.data_class.dto.booking.airplane

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetFlightOffersRequest(
    @SerializedName("originCity")
    val departure : String,
    @SerializedName("destinationCity")
    val destination : String,
    @SerializedName("departureDate")
    val departureDate : String,
    @SerializedName("returnDate")
    val returnDate : String,
    @SerializedName("adults")
    val adults : Long,
    @SerializedName("children")
    val children : Long,
    @SerializedName("max")
    val max : Long,
    @SerializedName("nonStop")
    val nonStop : Boolean
) : Serializable

data class GetOneWayFlightOffersRequest(
    @SerializedName("originCity")
    val departure : String,
    @SerializedName("destinationCity")
    val destination : String,
    @SerializedName("departureDate")
    val departureDate : String,
    @SerializedName("adults")
    val adults : Long,
    @SerializedName("children")
    val children : Long,
    @SerializedName("max")
    val max : Long,
    @SerializedName("nonStop")
    val nonStop : Boolean
) : Serializable
