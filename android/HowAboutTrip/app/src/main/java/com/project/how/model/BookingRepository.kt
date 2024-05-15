package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.GetFlightOffersResponse
import com.project.how.data_class.dto.LikeFlightList

class BookingRepository {
    private val _flightOffersLiveData = MutableLiveData<GetFlightOffersResponse>()
    val flightOffersLiveData : LiveData<GetFlightOffersResponse>
        get() = _flightOffersLiveData

    fun getFlightOffers(getFlightOffersResponse: GetFlightOffersResponse){
        _flightOffersLiveData.postValue(getFlightOffersResponse)
    }
}