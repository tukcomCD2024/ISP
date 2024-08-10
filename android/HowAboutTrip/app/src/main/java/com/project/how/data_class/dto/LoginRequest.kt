package com.project.how.data_class.dto
import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("uid") val uid : String
)