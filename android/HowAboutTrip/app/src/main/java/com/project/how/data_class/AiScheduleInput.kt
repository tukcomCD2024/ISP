package com.project.how.data_class

data class AiScheduleInput(
    val des : String,
    val purpose : List<String>?,
    val startDate : String,
    val endDate : String
)
