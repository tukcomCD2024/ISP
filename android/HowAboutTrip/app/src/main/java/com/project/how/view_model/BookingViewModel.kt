package com.project.how.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.R
import com.project.how.data_class.dto.GenerateOneWaySkyscannerUrlRequest
import com.project.how.data_class.dto.GenerateSkyscannerUrlRequest
import com.project.how.data_class.dto.GenerateSkyscannerUrlResponse
import com.project.how.data_class.dto.GetFlightOffersRequest
import com.project.how.data_class.dto.GetFlightOffersResponse
import com.project.how.data_class.dto.GetOneWayFlightOffersRequest
import com.project.how.data_class.dto.GetOneWayFlightOffersResponse
import com.project.how.model.BookingRepository
import com.project.how.network.client.BookingRetrofit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingViewModel : ViewModel() {
    private val bookingRepository = BookingRepository()
    private val _flightOffersLiveData = bookingRepository.flightOffersLiveData
    private val _oneWayFlightOffersLiveData = bookingRepository.oneWayFlightOffersLiveData
    private val _skyscannerUrlLiveData = bookingRepository.skyscannerUrlLiveData
    val flightOffersLiveData : LiveData<GetFlightOffersResponse>
        get() = _flightOffersLiveData
    val oneWayFlightOffersLiveData : LiveData<GetOneWayFlightOffersResponse>
        get() = _oneWayFlightOffersLiveData
    val skyscannerUrlLiveData : LiveData<String>
        get() = _skyscannerUrlLiveData


    fun getFlightOffers(context : Context, accessToken : String, getFlightOffersRequest: GetFlightOffersRequest) : Flow<Int> = callbackFlow{
        BookingRetrofit.getApiService()?.let {apiService ->
            apiService.getFlightOffers(context.getString(R.string.bearer_token, accessToken), getFlightOffersRequest)
                .enqueue(object : Callback<GetFlightOffersResponse>{
                    override fun onResponse(
                        call: Call<GetFlightOffersResponse>,
                        response: Response<GetFlightOffersResponse>
                    ) {
                        if (response.isSuccessful){
                            val result = response.body()
                            if (result != null){
                                Log.d("getFlightOffers is success", "result.size : ${result.size}")
                                if (result.isNotEmpty()){
                                    Log.d("getFlightOffers is success", "${result[0].departureIataCode} -> ${result[0].arrivalIataCode}")
                                    bookingRepository.getFlightOffers(result)
                                    trySend(SUCCESS)
                                }else{
                                    trySend(NOT_EXIST)
                                }
                            }else{
                                Log.d("getFlightOffers is success", "response.body is null\ncode : ${response.code()}")
                                trySend(NOT_EXIST)
                            }
                        }else{
                            Log.d("getFlightOffers is not success", "code : ${response.code()}")
                            trySend(NOT_EXIST)
                        }
                    }

                    override fun onFailure(call: Call<GetFlightOffersResponse>, t: Throwable) {
                        Log.d("getFlightOffers is failed", "${t.message}")
                        trySend(NETWORK_FAILED)
                    }

                })
        } ?: close()

        awaitClose()
    }

    fun getFlightOffers(context : Context, accessToken : String, getOneWayFlightOffersRequest: GetOneWayFlightOffersRequest) : Flow<Int> = callbackFlow{
        BookingRetrofit.getApiService()?.let {apiService ->
            apiService.getOneWayFligthOffers(context.getString(R.string.bearer_token, accessToken), getOneWayFlightOffersRequest)
                .enqueue(object : Callback<GetOneWayFlightOffersResponse>{
                    override fun onResponse(
                        call: Call<GetOneWayFlightOffersResponse>,
                        response: Response<GetOneWayFlightOffersResponse>
                    ) {
                        if (response.isSuccessful){
                            val result = response.body()
                            if (result != null){
                                Log.d("getFlightOffers is success", "result.size : ${result.size}")
                                if (result.isNotEmpty()){
                                    Log.d("getFlightOffers is success", "${result[0].departureIataCode} -> ${result[0].arrivalIataCode}")
                                    bookingRepository.getOneWayFlightOffers(result)
                                    trySend(SUCCESS)
                                }else{
                                    trySend(NOT_EXIST)
                                }
                            }else{
                                Log.d("getFlightOffers is success", "response.body is null\ncode : ${response.code()}")
                                trySend(NOT_EXIST)
                            }
                        }else{
                            Log.d("getFlightOffers is no6t success", "code : ${response.code()}")
                            trySend(NOT_EXIST)
                        }
                    }

                    override fun onFailure(call: Call<GetOneWayFlightOffersResponse>, t: Throwable) {
                        Log.d("getFlightOffers is failed", "${t.message}")
                        trySend(NETWORK_FAILED)
                    }

                })
        } ?: close()

        awaitClose()
    }

    fun generateSkyscannerUrl(context : Context, accessToken : String, generateSkyscannerUrlRequest: GenerateSkyscannerUrlRequest) : Flow<Int> = callbackFlow {
        BookingRetrofit.getApiService()?.let {apiService->
           apiService.generateSkyscannerUrl(context.getString(R.string.bearer_token, accessToken), generateSkyscannerUrlRequest)
               .enqueue(object : Callback<GenerateSkyscannerUrlResponse>{
                   override fun onResponse(
                       call: Call<GenerateSkyscannerUrlResponse>,
                       response: Response<GenerateSkyscannerUrlResponse>
                   ) {
                       if (response.isSuccessful){
                           val result = response.body()
                           if (result != null){
                               Log.d("generateSkyscannerUrl response is success", "url : ${result.url}")
                               bookingRepository.getSkyscannerUrl(result.url)
                               trySend(SUCCESS)
                           }else{
                               Log.d("generateSkyscannerUrl response is success", "response.body is null\ncode : ${response.code()}")
                               trySend(NOT_EXIST)
                           }
                       }else{
                           Log.d("generateSkyscannerUrl response is not success", "code : ${response.code()}")
                           trySend(NOT_EXIST)
                       }
                   }

                   override fun onFailure(call: Call<GenerateSkyscannerUrlResponse>, t: Throwable) {
                       Log.d("generateSkyscannerUrl is failed", "${t.message}")
                       trySend(NETWORK_FAILED)
                   }

               })
        } ?: close()

        awaitClose()
    }

    fun generateOneWaySkyscannerUrl(context : Context, accessToken : String, generateOneWaySkyscannerUrlRequest: GenerateOneWaySkyscannerUrlRequest) : Flow<Int> = callbackFlow {
        BookingRetrofit.getApiService()?.let {apiService->
            apiService.generateOneWaySkyscannerUrl(context.getString(R.string.bearer_token, accessToken), generateOneWaySkyscannerUrlRequest)
                .enqueue(object : Callback<GenerateSkyscannerUrlResponse>{
                    override fun onResponse(
                        call: Call<GenerateSkyscannerUrlResponse>,
                        response: Response<GenerateSkyscannerUrlResponse>
                    ) {
                        if (response.isSuccessful){
                            val result = response.body()
                            if (result != null){
                                Log.d("generateSkyscannerUrl response is success", "url : ${result.url}")
                                bookingRepository.getSkyscannerUrl(result.url)
                                trySend(SUCCESS)
                            }else{
                                Log.d("generateSkyscannerUrl response is success", "response.body is null\ncode : ${response.code()}")
                                trySend(NOT_EXIST)
                            }
                        }else{
                            Log.d("generateSkyscannerUrl response is not success", "code : ${response.code()}")
                            trySend(NOT_EXIST)
                        }
                    }

                    override fun onFailure(call: Call<GenerateSkyscannerUrlResponse>, t: Throwable) {
                        Log.d("generateSkyscannerUrl is failed", "${t.message}")
                        trySend(NETWORK_FAILED)
                    }

                })
        } ?: close()

        awaitClose()
    }

    companion object{
        const val NETWORK_FAILED = -1
        const val NOT_EXIST = -2
        const val SUCCESS = 0
    }
}