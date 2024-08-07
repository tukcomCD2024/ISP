package com.project.how.data_class.recyclerview.record

data class RecentAddedRecord(
    val id : Long,
    val startDate : String,
    val endDate : String?,
    val countryImage : String?,
    val image : String?,
    val title : String
)
