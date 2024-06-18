package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.GetFlightOffersResponse
import com.project.how.data_class.dto.GetLikeFlightResponse
import com.project.how.data_class.dto.GetOneWayFlightOffersResponse
import java.util.LinkedList

class BookingRepository {
    private val _flightOffersLiveData = MutableLiveData<GetFlightOffersResponse>()
    private val _oneWayFlightLiveData = MutableLiveData<GetOneWayFlightOffersResponse>()
    private val _skyscannerUrlLiveData = MutableLiveData<String>()
    private val _likeFlightLiveData = MutableLiveData<GetLikeFlightResponse>()
    private val _likeFlightListLiveData = MutableLiveData<MutableList<Long>>()
    private  var lfl = mutableListOf<Long>()
    val flightOffersLiveData : LiveData<GetFlightOffersResponse>
        get() = _flightOffersLiveData
    val oneWayFlightOffersLiveData : LiveData<GetOneWayFlightOffersResponse>
        get() = _oneWayFlightLiveData
    val skyscannerUrlLiveData : LiveData<String>
        get() = _skyscannerUrlLiveData
    val likeFlightLiveData : LiveData<GetLikeFlightResponse>
        get() = _likeFlightLiveData
    val likeFlightListLiveData : LiveData<MutableList<Long>>
        get() = _likeFlightListLiveData



    fun getFlightOffers(getFlightOffersResponse: GetFlightOffersResponse){
        _flightOffersLiveData.postValue(getFlightOffersResponse)
    }

    fun getOneWayFlightOffers(getOneWayFlightOffersResponse: GetOneWayFlightOffersResponse){
        _oneWayFlightLiveData.postValue(getOneWayFlightOffersResponse)
    }

    fun getSkyscannerUrl(url : String){
        _skyscannerUrlLiveData.postValue(url)
    }

    fun getLikeFlight(data : GetLikeFlightResponse){
        lfl = data.map { it.id }.toMutableList()
        _likeFlightLiveData.postValue(data)
    }

    fun getLikeFlightList(data : MutableList<Long>){
        lfl = data
        _likeFlightListLiveData.postValue(data)
    }

    fun addLikeFlightList(data : Long, position : Int){
        lfl[position] = data
        _likeFlightListLiveData.postValue(lfl)
    }

    fun deleteLikeFlightList(position: Int){
        lfl[position] = -1L
        _likeFlightListLiveData.postValue(lfl)
    }
}