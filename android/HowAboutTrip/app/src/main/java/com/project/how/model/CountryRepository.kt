package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.TargetExchangeRate
import com.project.how.data_class.dto.country.exchangerate.GetAllExchangeRatesResponse
import com.project.how.data_class.dto.country.weather.GetCurrentWeatherResponse
import com.project.how.data_class.dto.country.weather.GetWeeklyWeathersResponse

class CountryRepository {
    private var _currentWeatherLiveData : MutableLiveData<GetCurrentWeatherResponse> = MutableLiveData()
    private var _weeklyWeathersLiveData : MutableLiveData<GetWeeklyWeathersResponse> = MutableLiveData()
    private var _exchangeRatesLiveData : MutableLiveData<GetAllExchangeRatesResponse> = MutableLiveData()
    private var _targetExchageRateLiveData : MutableLiveData<TargetExchangeRate> = MutableLiveData()
    val currentWeatherLiveData : LiveData<GetCurrentWeatherResponse>
        get() = _currentWeatherLiveData
    val weeklyWeathersLiveData : LiveData<GetWeeklyWeathersResponse>
        get() = _weeklyWeathersLiveData
    val exchangeRatesLiveData : LiveData<GetAllExchangeRatesResponse>
        get() = _exchangeRatesLiveData
    val targetExchangeRateLiveData : LiveData<TargetExchangeRate>
        get() = _targetExchageRateLiveData

    fun getCurrentWeather(data : GetCurrentWeatherResponse){
        _currentWeatherLiveData.postValue(data)
    }

    fun getWeeklyWeathers(data : GetWeeklyWeathersResponse){
        _weeklyWeathersLiveData.postValue(data)
    }

    fun getExchangeRates(data : GetAllExchangeRatesResponse){
        _exchangeRatesLiveData.postValue(data)
    }

    fun getTargetExchangeRateLiveData(data : TargetExchangeRate){
        _targetExchageRateLiveData.postValue(data)
    }

}