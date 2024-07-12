package com.project.how.data_class.dto.schedule

data class GetFastestSchedulesResponse(
    val id: Long,
    val scheduleName: String,
    val dday: String,
    val country: String,
    val imageUrl: String
)
