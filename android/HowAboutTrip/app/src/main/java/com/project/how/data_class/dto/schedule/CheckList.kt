package com.project.how.data_class.dto.schedule

import com.google.gson.annotations.SerializedName

typealias CheckList = List<CheckListElement>

data class CheckListElement(
    @SerializedName("id") val id : Long,
    @SerializedName("todo") var todo : String,
    @SerializedName("check") var check : Boolean
)
