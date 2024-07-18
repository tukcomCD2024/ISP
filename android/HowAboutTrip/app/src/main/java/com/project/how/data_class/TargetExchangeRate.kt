package com.project.how.data_class

data class TargetExchangeRate(
    val targetUnit : String,
    val targetUnitCode : String,
    val targetCountry : String,
    val targetUnitWonExchangeRate : Double,
    val targetUnitDollarExchangeRate : Double,
    val targetUnitStandard : Double
)
