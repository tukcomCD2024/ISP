package com.project.how.data_class

data class AiSchedule(
    val title: String,
    val places : List<String>,
    val image : String,
    val startDate : String,
    val endDate : String,
    val dailySchedule: List<List<AiDaysSchedule>>
)

data class AiDaysSchedule(
    val type : Int,
    val todo: String,
    val places: String
)