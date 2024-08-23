package com.project.how.data_class.dto.schedule

import com.google.gson.annotations.SerializedName

typealias AddCheckListsRequest = List<AddCheckListsRequestElement>


data class AddCheckListsRequestElement(
    @SerializedName("todo") val todo : String,
    @SerializedName("check") val check : Boolean
)