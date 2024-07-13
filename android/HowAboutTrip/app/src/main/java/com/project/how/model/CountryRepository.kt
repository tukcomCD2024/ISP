package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.country.weather.GetCurrentWeatherResponse
import com.project.how.data_class.dto.country.weather.GetWeeklyWeathersResponse

class CountryRepository {
    private var _currentWeatherLiveData : MutableLiveData<GetCurrentWeatherResponse> = MutableLiveData()
    private var _weeklyWeathersLiveData : MutableLiveData<GetWeeklyWeathersResponse> = MutableLiveData()
    val currentWeatherLiveData : LiveData<GetCurrentWeatherResponse>
        get() = _currentWeatherLiveData
    val weeklyWeathersLiveData : LiveData<GetWeeklyWeathersResponse>
        get() = _weeklyWeathersLiveData

    fun getCurrentWeather(data : GetCurrentWeatherResponse){
        _currentWeatherLiveData.postValue(data)
    }

    fun getWeeklyWeathers(data : GetWeeklyWeathersResponse){
        _weeklyWeathersLiveData.postValue(data)
    }

}