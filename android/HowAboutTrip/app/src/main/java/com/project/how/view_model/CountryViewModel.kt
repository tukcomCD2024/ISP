package com.project.how.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.data_class.TargetExchangeRate
import com.project.how.data_class.dto.country.GetCountryInfoRequest
import com.project.how.data_class.dto.country.GetCountryLocationResponse
import com.project.how.data_class.dto.country.exchangerate.GetAllExchangeRatesResponse
import com.project.how.data_class.dto.country.exchangerate.GetAllExchangeRatesResponseElement
import com.project.how.data_class.dto.country.weather.GetCurrentWeatherResponse
import com.project.how.data_class.dto.country.weather.GetWeeklyWeathersResponse
import com.project.how.model.CountryRepository
import com.project.how.network.client.CountryRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

class CountryViewModel : ViewModel() {
    private var countryRepository : CountryRepository = CountryRepository()
    private val _currentWeatherLiveData = countryRepository.currentWeatherLiveData
    private val _weeklyWeathersLiveData = countryRepository.weeklyWeathersLiveData
    private val _exchangeRatesLiveData = countryRepository.exchangeRatesLiveData
    val currentWeatherLiveData : LiveData<GetCurrentWeatherResponse>
        get() = _currentWeatherLiveData
    val weeklyWeatherLiveData : LiveData<GetWeeklyWeathersResponse>
        get() = _weeklyWeathersLiveData
    val exchangeRatesLiveData : LiveData<GetAllExchangeRatesResponse>
        get() = _exchangeRatesLiveData



    fun getCountryLocation(country : String) : Flow<GetCountryLocationResponse?> = callbackFlow {
        CountryRetrofit.getApiService()?.let { apiService ->
            apiService.getCountryLocation(
                GetCountryInfoRequest(
                    country
                )
            )
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
    }.flowOn(Dispatchers.IO)

    fun getCurrentWeather(country: String) : Flow<Boolean> = callbackFlow {
        CountryRetrofit.getApiService()?.let { apiService->
            apiService.getCurrentWeather(
                GetCountryInfoRequest(
                    country
                )
            )
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
            apiService.getWeeklyWeather(
                GetCountryInfoRequest(
                    country
                )
            )
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
                        Log.d("getWeeklyWeather", "onFailed${p1.message}")
                        trySend(false)
                    }

                })

        } ?: close()

        awaitClose()
    }

    private fun getAllExchangeRates() : Flow<GetAllExchangeRatesResponse?> = callbackFlow {
        CountryRetrofit.getApiService()?.let { apiService->
            apiService.getAllExchangeRates()
                .enqueue(object : Callback<GetAllExchangeRatesResponse>{
                    override fun onResponse(
                        p0: Call<GetAllExchangeRatesResponse>,
                        p1: Response<GetAllExchangeRatesResponse>
                    ) {
                        if (p1.isSuccessful){
                            val result = p1.body()
                            if (result != null){
                                Log.d("getAllExchangeRates", "response body : ${result[0].baseCurrency}\t${result[0].targetCurrency}\t${result[0].rate}")
                                countryRepository.getExchangeRates(result)
                                trySend(result)
                            }else{
                                Log.d("getAllExchangeRates", "response body is null\ncode : ${p1.code()}")
                                trySend(null)
                            }
                        }else{
                            Log.d("getAllExchangeRates", "response is failed\ncode : ${p1.code()}")
                            trySend(null)
                        }
                    }

                    override fun onFailure(p0: Call<GetAllExchangeRatesResponse>, p1: Throwable) {
                        Log.d("getAllExchangeRates", "onFailed\n${p1.message}")
                        trySend(null)
                    }

                })
        } ?: close()

        awaitClose()
    }

    private fun getExchangeRate(unitCode : String) : Flow<List<GetAllExchangeRatesResponseElement>?> = flow{
        var result = listOf<GetAllExchangeRatesResponseElement>()
        exchangeRatesLiveData.value?.let { allExchangeRates->
            result = allExchangeRates.filter { it.targetCurrency == unitCode }
            emit(result)
        } ?: let{
            getAllExchangeRates().collect{ allExchangeRates->
                if (!allExchangeRates.isNullOrEmpty()){
                    result = allExchangeRates.filter { it.targetCurrency == unitCode }
                    emit(result)
                }else{
                    Log.d("getExchangeRate", "getAllExchangeRates is failed, so getExchangeRate is failed.\ncheck getExchangeRate and getAllExchangeRate")
                    emit(null)
                }
            }
        }
    }

    private fun getTargetUnitCodeAndUnit(country : String) : MutableList<String>{
        var unit = mutableListOf<String>()
        when(country){
            "스위스" -> {
                unit.add("CHF")
                unit.add("프랑")
            }
            "체코" -> {
                unit.add("CZK")
                unit.add("코루나")
            }
            "영국" -> {
                unit.add("GBP")
                unit.add("파운드")
            }
            "인도네시아" -> {
                unit.add("IDR")
                unit.add("루피아")
            }
            "일본" -> {
                unit.add("JPY")
                unit.add("엔")
            }
            "라오스" -> {
                unit.add("LAK")
                unit.add("킵")
            }
            "말레이시아" -> {
                unit.add("MYR")
                unit.add("링깃")
            }
            "필리핀" -> {
                unit.add("PHP")
                unit.add("페소")
            }
            "싱가포르" -> {
                unit.add("SGD")
                unit.add("달러")
            }
            "태국" -> {
                unit.add("THB")
                unit.add("바트")
            }
            "대만" -> {
                unit.add("TWD")
                unit.add("달러")
            }
            "미국" -> {
                unit.add("USD")
                unit.add("달러")
            }
            "베트남" -> {
                unit.add("VND")
                unit.add("동")
            }
            "한국" -> {
                unit.add("KRW")
                unit.add("원")
            }
            "유럽연합" -> {
                unit.add("EUR")
                unit.add("유로")
            }
            else -> {
                unit.add(UNKNOWN)
                unit.add(UNKNOWN)
                Log.d("getTargetCountry", "targetUnitCode : ${country}")
            }
        }
        return unit
    }

    suspend fun getTargetExchangeRate(country: String): TargetExchangeRate? {
        val codeAndUnit = getTargetUnitCodeAndUnit(country)
        val targetUnit: String = codeAndUnit[1]
        val targetUnitCode: String = codeAndUnit[0]
        val targetCountry: String = country
        var targetUnitWonExchangeRate: Double = 0.0
        var targetUnitDollarExchangeRate: Double = 0.0
        var targetUnitStandard = 0.0

        if (targetUnit == UNKNOWN) {
            return null
        }

        try {
            val exchangeRates = getExchangeRate(targetUnitCode).firstOrNull()
            if (exchangeRates != null) {
                Log.d("getTargetExchangeRate", "filtered exchangeRates.size = ${exchangeRates.size}")
                if (targetUnitCode == "KRW") {
                    targetUnitWonExchangeRate = 1.0
                    targetUnitDollarExchangeRate = exchangeRates.last().rate
                    targetUnitStandard = THOUSAND
                } else if (targetUnitCode == "USD") {
                    targetUnitWonExchangeRate = exchangeRates.last().rate
                    targetUnitDollarExchangeRate = 1.0
                    targetUnitStandard = ONE
                } else {
                    targetUnitWonExchangeRate = exchangeRates[0].rate
                    targetUnitDollarExchangeRate = exchangeRates[1].rate
                    if ((targetUnitWonExchangeRate < 1.0) || (targetUnitDollarExchangeRate < 1.0))
                        targetUnitStandard = THOUSAND
                    else
                        targetUnitStandard = ONE
                }
                Log.d("getTargetExchangeRate", "getExchangeRate is finished($targetCountry : $targetUnitCode)\ntargetUnitWonExchangeRate : $targetUnitWonExchangeRate\ntargetUnitDollarExchangeRate : $targetUnitDollarExchangeRate")
            }
        } catch (e: Exception) {
            Log.e("getTargetExchangeRate", "Error during getExchangeRate: ${e.message}")
            return null
        }

        Log.d("getTargetExchangeRate", "targetUnit : $targetUnit\ntargetUnitCode : $targetUnitCode\ntargetCountry : $targetCountry\ntargetUnitWonExchangeRate : $targetUnitWonExchangeRate\ntargetUnitDollarExchangeRate : $targetUnitDollarExchangeRate")
        return TargetExchangeRate(
            targetUnit,
            targetUnitCode,
            targetCountry,
            targetUnitWonExchangeRate,
            targetUnitDollarExchangeRate,
            targetUnitStandard
        )
    }

    private fun calculateCurrency(standard: Double, wonRate: Double, dollarRate: Double) : List<Double>{
        val result = mutableListOf<Double>()
        result.add(standard/wonRate)
        result.add(standard/dollarRate)
        return result
    }

    fun getCurrency(input : Double, wonRate : Double, dollarRate : Double) : List<String>{
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val v = calculateCurrency(input, wonRate, dollarRate)
        return listOf(df.format(v[0]), df.format(v[1]))
    }

    fun getTargetValue(rate: Double, input: Double) : String{
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        return df.format(rate * input)
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
                m = UNKNOWN
                Log.d("changeMainToKorea(Weather)", "main : ${main}")
            }
        }

        return m

    }

    companion object{
        const val UNKNOWN = "알 수 없음"
        const val THOUSAND = 1000.0
        const val ONE= 1.0
        const val KRW = 0
    }
}