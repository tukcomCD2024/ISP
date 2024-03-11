package com.project.how.data_class

import java.io.Serializable

class Schedule (
    var title: String,
    var country : String,
    var startDate : String,
    var endDate : String,
    var cost: Long,
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