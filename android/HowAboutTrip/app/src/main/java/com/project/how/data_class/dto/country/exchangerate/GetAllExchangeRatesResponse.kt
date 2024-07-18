package com.project.how.data_class.dto.country.exchangerate

typealias GetAllExchangeRatesResponse = ArrayList<GetAllExchangeRatesResponseElement>

data class GetAllExchangeRatesResponseElement (
    val baseCurrency: BaseCurrency,
    val targetCurrency: String,
    val rate: Double
)

enum class BaseCurrency {
    KRW,
    USD
}
