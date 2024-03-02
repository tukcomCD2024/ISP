package com.project.how.data_class.dto

data class ScheduleDetail (
    val scheduleName: String,
    val country : String,
    val startDate: String,
    val endDate: String,
    val dailySchedules: List<DailySchedule>
)

data class DailySchedule (
    val date: String,
    val schedules: List<Schedule>
)

data class Schedule (
    val todo: String,
    val place: String,
    val budget: Long,
    val latitude: Double,
    val longitude: Double
)
