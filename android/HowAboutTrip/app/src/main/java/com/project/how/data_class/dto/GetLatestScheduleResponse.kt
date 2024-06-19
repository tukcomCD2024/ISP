package com.project.how.data_class.dto

typealias GetLatestSchedulesResponse = List<GetLatestSchedulesResponseElement>

data class GetLatestSchedulesResponseElement (
    val id: Long,
    val scheduleName: String,
    val city: String,
    val imageUrl: String,
    val plan: List<String>
)
