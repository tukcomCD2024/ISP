package com.project.how.data_class

data class AiScheduleListInput(
    val des : String,
    val purpose : List<String>?,
    val activities : List<String>?,
    val excludingActivity : List<String>?,
    val startDate : String,
    val endDate : String
)
