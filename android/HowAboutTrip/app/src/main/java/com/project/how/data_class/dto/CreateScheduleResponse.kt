package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

typealias CreateScheduleListResponse = List<CreateScheduleResponse>

data class CreateScheduleResponse (
    @SerializedName("countryImage") val countryImage : String,
    @SerializedName("schedules") val schedules: List<AiSimpleSchedule>
)

data class AiSimpleSchedule (
    @SerializedName("date") val date: String,
    @SerializedName("scheduleDetail") val scheduleDetail: List<String>
)
