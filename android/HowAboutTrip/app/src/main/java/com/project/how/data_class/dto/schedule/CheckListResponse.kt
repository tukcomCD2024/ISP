package com.project.how.data_class.dto.schedule

import com.google.gson.annotations.SerializedName

typealias CheckListResponse = List<CheckListResponseElement>

data class CheckListResponseElement(
    @SerializedName("id") val id : Long,
    @SerializedName("todo") val todo : String,
    @SerializedName("check") val check : Boolean
)
