package com.project.how.data_class.dto.country.weather

import com.google.gson.annotations.SerializedName

data class GetCurrentWeatherResponse (
    val main: String,
    val description: String,
    val temp: String,
    val localTime: String,
    val iconUrl: String,
    @SerializedName("temp_min")
    val tempMin: String,
    @SerializedName("temp_max")
    val tempMax: String
)