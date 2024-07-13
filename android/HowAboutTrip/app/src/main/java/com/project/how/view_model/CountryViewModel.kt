package com.project.how.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.data_class.dto.country.GetCountryInfoRequest
import com.project.how.data_class.dto.country.GetCountryLocationResponse
import com.project.how.data_class.dto.country.weather.GetCurrentWeatherResponse
import com.project.how.data_class.dto.country.weather.GetWeeklyWeathersResponse
import com.project.how.model.CountryRepository
import com.project.how.network.client.CountryRetrofit
import com.project.how.network.client.ScheduleRetrofit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryViewModel : ViewModel() {
    private var countryRepository : CountryRepository = CountryRepository()
    private val _currentWeatherLiveData = countryRepository.currentWeatherLiveData
    private val _weeklyWeathersLiveData = countryRepository.weeklyWeathersLiveData
    val currentWeatherLiveData : LiveData<GetCurrentWeatherResponse>
        get() = _currentWeatherLiveData
    val weeklyWeatherLiveData : LiveData<GetWeeklyWeathersResponse>
        get() = _weeklyWeathersLiveData

    fun getCountryLocation(country : String) : Flow<GetCountryLocationResponse?> = callbackFlow {
        CountryRetrofit.getApiService()?.let { apiService ->
            apiService.getCountryLocation(GetCountryInfoRequest(country))
                .enqueue(object : Callback<GetCountryLocationResponse> {
                    override fun onResponse(
                        call: Call<GetCountryLocationResponse>,
                        response: Response<GetCountryLocationResponse>
                    ) {
                        if (response.isSuccessful){
                            val result = response.body()
                            if (result != null){
                                Log.d("getCountryLocation", "country : $country\nlat : ${result.lat}\tlng : ${result.lng}")
                                trySend(result)
                                close()
                            }else{
                                Log.d("getCountryLocation", "response code : ${response.code()}")
                                trySend(null)
                            }
                        }else{
                            Log.d("getCountryLocation", "response code : ${response.code()}")
                            trySend(null)
                        }
                    }

                    override fun onFailure(call: Call<GetCountryLocationResponse>, t: Throwable) {
                        Log.d("getCountryLocation", "${t.message}")
                        trySend(null)
                    }

                })
        } ?: close()

        awaitClose()
    }

    fun getCurrentWeather(country: String) : Flow<Boolean> = callbackFlow {
        CountryRetrofit.getApiService()?.let { apiService->
            apiService.getCurrentWeather(GetCountryInfoRequest(country))
                .enqueue(object : Callback<GetCurrentWeatherResponse>{
                    override fun onResponse(
                        p0: Call<GetCurrentWeatherResponse>,
                        p1: Response<GetCurrentWeatherResponse>
                    ) {
                        if (p1.isSuccessful){
                            val result = p1.body()
                            if (result != null){
                                Log.d("getCurrentWeather", "country : $country\ntemp : ${result.temp}")
                                countryRepository.getCurrentWeather(result)
                                trySend(true)
                            }else{
                                Log.d("getCurrentWeather", "response.body is null")
                                trySend(false)
                            }

                        }else{
                            Log.d("getCurrentWeather", "response is failed.")
                            trySend(false)
                        }
                    }

                    override fun onFailure(p0: Call<GetCurrentWeatherResponse>, p1: Throwable) {
                        Log.d("getCurrentWeather", "${p1.message}")
                        trySend(false)
                    }

                })
        } ?: close()

        awaitClose()
    }

    fun getWeeklyWeathers(country: String) : Flow<Boolean> = callbackFlow {
        CountryRetrofit.getApiService()?.let { apiService->
            apiService.getWeeklyWeather(GetCountryInfoRequest(country))
                .enqueue(object : Callback<GetWeeklyWeathersResponse>{
                    override fun onResponse(
                        p0: Call<GetWeeklyWeathersResponse>,
                        p1: Response<GetWeeklyWeathersResponse>
                    ) {
                        if (p1.isSuccessful){
                           val result = p1.body()
                           if (result != null){
                               Log.d("getWeeklyWeathers", "length : ${result.size}")
                               countryRepository.getWeeklyWeathers(result)
                               trySend(true)
                           }else{
                               Log.d("getWeeklyWeathers", "response.body is null")
                               trySend(false)
                           }
                        }else{
                            Log.d("getWeeklyWeathers", "response is failed.")
                            trySend(false)
                        }
                    }

                    override fun onFailure(p0: Call<GetWeeklyWeathersResponse>, p1: Throwable) {
                        Log.d("getWeeklyWeather", "${p1.message}")
                        trySend(false)
                    }

                })

        } ?: close()

        awaitClose()
    }

    fun changeMainToKorean(main : String) : String{
        var m = ""

        when(main){
            "Clear" -> m = "맑음"
            "Clouds" -> m = "흐림"
            "Thunderstorm" -> m = "번개"
            "Drizzle" -> m = "이슬비"
            "Rain" -> m = "비"
            "Snow" -> m = "눈"
            "Mist" -> m = "안개"
            "Smoke" -> m = "연기"
            "Haze" -> m = "옅은 안개"
            "Fog" -> m = "짙은 안개"
            "Dust" -> m = "먼지"
            "Ash" -> m = "화산재"
            "Squalls" -> m = "돌풍"
            "Tornado" -> m = "폭풍"
            else -> {
                m = "알 수 없음"
                Log.d("changeMainToKorea(Weather)", "main : ${main}")
            }
        }

        return m

    }
}