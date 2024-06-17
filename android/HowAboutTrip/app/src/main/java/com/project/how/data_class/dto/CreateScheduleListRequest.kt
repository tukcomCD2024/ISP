package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

data class CreateScheduleListRequest(
    @SerializedName("destination")
    val destination : String,
    @SerializedName("purpose")
    val purpose : List<String>,
    @SerializedName("includedActivities")
    val activities : List<String>,
    @SerializedName("excludedActivities")
    val excludingActivity : List<String>,
    @SerializedName("departureDate")
    val departureDate : String,
    @SerializedName("returnDate")
    val returnDate : String
)
