package com.project.how.data_class.dto

typealias GetScheduleListResponse = List<GetScheduleListResponseElement>

data class GetScheduleListResponseElement (
    val id: Long,
    val scheduleName: String,
    val startDate: String,
    val endDate: String,
    val totalPrice: Long,
    val imageURL: String
)
