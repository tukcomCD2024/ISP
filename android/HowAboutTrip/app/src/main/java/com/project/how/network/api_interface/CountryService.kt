package com.project.how.network.api_interface

import com.project.how.data_class.dto.country.GetCountryInfoRequest
import com.project.how.data_class.dto.country.GetCountryLocationResponse
import com.project.how.data_class.dto.country.weather.GetCurrentWeatherResponse
import com.project.how.data_class.dto.country.weather.GetWeeklyWeathersResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CountryService {
    @POST("countries/locations")
    fun getCountryLocation(
        @Body country : GetCountryInfoRequest
    ) : Call<GetCountryLocationResponse>

    @POST("countries/weather/current")
    fun getCurrentWeather(
        @Body country: GetCountryInfoRequest
    ) : Call<GetCurrentWeatherResponse>

    @POST("countries/weather/weekly")
    fun getWeeklyWeather(
        @Body country: GetCountryInfoRequest
    ) : Call<GetWeeklyWeathersResponse>
}