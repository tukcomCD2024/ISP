package com.project.how.data_class.recyclerview.record

data class Bill(
    val id : Long,
    val image : String?,
    val title : String,
    val date : String,
    val cost : Long,
    val count : Long
)
