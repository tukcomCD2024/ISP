package com.project.how.data_class

import java.io.Serializable

data class ScheduleIDAndLatLng(
    val id : Long,
    val latitude : Double,
    val longitude : Double
) : Serializable
