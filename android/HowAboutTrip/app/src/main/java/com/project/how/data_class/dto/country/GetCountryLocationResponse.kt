package com.project.how.data_class.dto.country

import com.google.gson.annotations.SerializedName

data class GetCountryLocationResponse(
    @SerializedName("latitude") val lat : Double,
    @SerializedName("longitude") val lng : Double,
    @SerializedName("currencyName") val currency : String
)
