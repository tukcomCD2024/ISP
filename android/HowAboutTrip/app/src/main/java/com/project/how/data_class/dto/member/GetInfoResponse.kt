package com.project.how.data_class.dto.member

import com.google.gson.annotations.SerializedName

data class GetInfoResponse(
    @SerializedName("uid") val uid : String,
    @SerializedName("name") val name : String?,
    @SerializedName("birth") val birth : String?,
    @SerializedName("phoneNumber") val phone : String?
)
