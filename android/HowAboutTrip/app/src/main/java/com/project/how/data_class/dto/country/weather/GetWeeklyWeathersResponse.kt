package com.project.how.data_class.dto.country.weather

import com.google.gson.annotations.SerializedName

typealias GetWeeklyWeathersResponse = ArrayList<GetWeeklyWeathersResponseElement>

data class GetWeeklyWeathersResponseElement (
    @SerializedName("date")
    val date: String,
    @SerializedName("temp")
    val temp: String,
    @SerializedName("iconUrl")
    val iconUrl: String
)
