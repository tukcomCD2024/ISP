package com.project.how.data_class.dto

import com.google.gson.annotations.SerializedName

data class ScheduleDetail (
    @SerializedName("scheduleName") val scheduleName: String,
    @SerializedName("country") val country : String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("dailySchedules") val dailySchedules: List<DailySchedule>
)

data class DailySchedule (
    @SerializedName("date") val date: String,
    @SerializedName("schedules") val schedules: List<Schedule>
)

data class Schedule (
    @SerializedName("todo") val todo: String,
    @SerializedName("place") val place: String,
    @SerializedName("type") val type : String,
    @SerializedName("budget") val budget: Long,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
)
