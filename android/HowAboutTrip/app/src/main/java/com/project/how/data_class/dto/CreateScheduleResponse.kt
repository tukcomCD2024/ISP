package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

data class CreateScheduleListResponse(
    @SerializedName("countryImage") val countryImage : String,
    @SerializedName("schedules") val schedules : List<CreateScheduleResponse>
)

data class CreateScheduleResponse (
    @SerializedName("schedule") val schedules: List<AiSimpleSchedule>
)

data class AiSimpleSchedule (
    @SerializedName("date") val date: String,
    @SerializedName("scheduleDetail") val scheduleDetail: List<AiScheduleDetail>
)

data class AiScheduleDetail (
    @SerializedName("detail") val detail: String,
    @SerializedName("coordinate") val coordinate: Coordinate
)

data class Coordinate (
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)

