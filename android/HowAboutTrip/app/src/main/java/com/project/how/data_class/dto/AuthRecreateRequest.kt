package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

data class AuthRecreateRequest(
    @SerializedName("refreshToken") val refreshToken : String
)
