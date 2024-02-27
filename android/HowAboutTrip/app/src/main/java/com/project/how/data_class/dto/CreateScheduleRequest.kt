package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

data class CreateScheduleRequest(
    @SerializedName("destination")
    val destination : String,
    @SerializedName("purpose")
    val purpose : List<String>,
    @SerializedName("departureDate")
    val departureDate : String,
    @SerializedName("returnDate")
    val returnDate : String
)
