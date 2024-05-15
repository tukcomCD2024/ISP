package com.project.how.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.R
import com.project.how.data_class.dto.GetFlightOffersRequest
import com.project.how.data_class.dto.GetFlightOffersResponse
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
    val flightOffersLiveData : LiveData<GetFlightOffersResponse>
        get() = _flightOffersLiveData

    fun getFlightOffers(context : Context, accessToken : String, getFlightOffersRequest: GetFlightOffersRequest) : Flow<Boolean> = callbackFlow{
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
                                Log.d("getFlightOffers is success", "result.size : ${result.size}\n${result[0].departureIataCode} -> ${result[0].arrivalIataCode}")
                                bookingRepository.getFlightOffers(result)
                                trySend(true)
                            }else{
                                Log.d("getFlightOffers is success", "response.body is null\ncode : ${response.code()}")
                                trySend(false)
                            }
                        }else{
                            Log.d("getFlightOffers is not success", "code : ${response.code()}")
                            trySend(false)
                        }
                    }

                    override fun onFailure(call: Call<GetFlightOffersResponse>, t: Throwable) {
                        Log.d("getFlightOffers is failed", "${t.message}")
                        trySend(false)
                    }

                })
        } ?: close()

        awaitClose()
    }
}