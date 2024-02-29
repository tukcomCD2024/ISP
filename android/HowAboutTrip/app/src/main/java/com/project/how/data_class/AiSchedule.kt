package com.project.how.data_class

import java.io.Serializable

class AiSchedule(
    val title: String,
    val country : String,
    val places : List<String>,
    val image : String,
    val startDate : String,
    val endDate : String,
    val dailySchedule: List<List<AiDaysSchedule>>
) : Serializable{

}

data class AiDaysSchedule(
    val type : Int,
    val todo: String,
    val places: String
) : Serializable
