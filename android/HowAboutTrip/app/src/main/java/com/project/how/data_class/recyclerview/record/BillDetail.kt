package com.project.how.data_class.recyclerview.record

data class BillDetail(
    var type : Int,
    var title : String,
    var totalCost : Double,
    var currencyCode : String,
    var startDate : String,
    var endDate : String,
    var merchant : String,
    var entries : List<Entries>
)

data class Entries(
    val date : String,
    val title : String,
    val cost : Double,
)
