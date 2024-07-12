package com.project.how.data_class.dto.weather

import com.google.gson.annotations.SerializedName

data class GetWeathersRequest(
    @SerializedName("country")
    val country : String
)
