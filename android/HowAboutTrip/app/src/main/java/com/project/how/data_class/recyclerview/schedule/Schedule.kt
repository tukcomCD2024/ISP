package com.project.how.data_class.recyclerview.schedule

import java.io.Serializable

class Schedule (
    var title: String,
    var country : String,
    var currency : String,
    var startDate : String,
    var endDate : String,
    var cost: Double,
    var dailySchedule: MutableList<MutableList<DaysSchedule>>
) : Serializable

data class DaysSchedule(
    var type : Int,
    val todo: String,
    val places: String,
    val latitude : Double?,
    val longitude : Double?,
    val cost : Double,
    val purchaseStatus : Boolean,
    val purchaseDate : String?
) : Serializable