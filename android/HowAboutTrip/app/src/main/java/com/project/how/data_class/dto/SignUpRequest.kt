package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("name") val name : String,
    @SerializedName("birth") val birth : String,
    @SerializedName("phoneNumber") val phone : String
)
