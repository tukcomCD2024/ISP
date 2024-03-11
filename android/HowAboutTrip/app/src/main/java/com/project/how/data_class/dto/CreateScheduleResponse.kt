package com.project.how.data_class.dto

data class CreateScheduleResponse (
    val schedules: List<AiSimpleSchedule>
)

data class AiSimpleSchedule (
    val date: String,
    val scheduleDetail: List<String>
)
