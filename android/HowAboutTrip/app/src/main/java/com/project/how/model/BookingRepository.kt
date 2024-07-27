package com.project.how.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.booking.airplane.GetFlightOffersResponse
import com.project.how.data_class.dto.booking.airplane.GetLikeFlightResponse
import com.project.how.data_class.dto.booking.airplane.GetOneWayFlightOffersResponse
import com.project.how.data_class.roomdb.RecentAirplane
import com.project.how.roomdb.dao.RecentAirplaneDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepository @Inject constructor(
    private val recentAirplaneDao : RecentAirplaneDao
) {
    private val _flightOffersLiveData = MutableLiveData<GetFlightOffersResponse>()
    private val _oneWayFlightLiveData = MutableLiveData<GetOneWayFlightOffersResponse>()
    private val _skyscannerUrlLiveData = MutableLiveData<String>()
    private val _likeFlightLiveData = MutableLiveData<GetLikeFlightResponse>()
    private val _likeFlightListLiveData = MutableLiveData<MutableList<Long>>()
    private val _recentAirplaneLiveData = MutableLiveData<List<RecentAirplane>>()
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
    val recentAirplaneLiveData : LiveData<List<RecentAirplane>>
        get() = _recentAirplaneLiveData

    init {
        getLikeFlightList(mutableListOf<Long>())
    }


    suspend fun fetchRecentAirplanes(): List<RecentAirplane> {
        return recentAirplaneDao.getRecentAirplanes()
    }

    suspend fun addRecentAirplane(recentAirplane: RecentAirplane) {
        Log.d("RoomDB", "addRecentAirplane()\nid : ${recentAirplane.id}\nname : ${recentAirplane.name}\ntime1 : ${recentAirplane.time1}\ncreateAt : ${recentAirplane.createdAt}\nurl : ${recentAirplane.skyscannerUrl}")
        recentAirplaneDao.insert(recentAirplane)
    }

    suspend fun deleteAllRecentAirplane(){
        recentAirplaneDao.deleteAll()
    }

    fun getRecentAirplane(data : List<RecentAirplane>){
        _recentAirplaneLiveData.postValue(data)
    }

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