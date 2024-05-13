package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

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
)
