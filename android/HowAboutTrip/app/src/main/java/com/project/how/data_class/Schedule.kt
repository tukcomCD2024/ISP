package com.project.how.data_class

import java.io.Serializable

class Schedule (
    val title: String,
    val country : String,
    val startDate : String,
    val endDate : String,
    val cost: Long,
    val dailySchedule: MutableList<MutableList<DaysSchedule>>
) : Serializable

data class DaysSchedule(
    var type : Int,
    val todo: String,
    val places: String,
    val latitude : Double?,
    val longitude : Double?,
    val cost : Long,
    val purchaseStatus : Boolean,
    val purchaseDate : String?
) : Serializable