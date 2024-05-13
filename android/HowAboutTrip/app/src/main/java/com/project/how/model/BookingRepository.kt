package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.LikeFlightList

class BookingRepository {
    private val _flightOffersLiveData = MutableLiveData<LikeFlightList>()
    val flightOffersLiveData : LiveData<LikeFlightList>
        get() = _flightOffersLiveData
}