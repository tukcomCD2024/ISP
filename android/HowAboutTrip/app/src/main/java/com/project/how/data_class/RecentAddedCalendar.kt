package com.project.how.data_class

data class RecentAddedCalendar(
    val id : Long,
    val des : String,
    val places : MutableList<String>,
    val image : String
)
