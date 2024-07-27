package com.project.how.data_class.dto.booking.airplane

import com.google.gson.annotations.SerializedName

data class GenerateSkyscannerUrlResponse(
    @SerializedName("skyscannerUrl")
    val url : String
)
