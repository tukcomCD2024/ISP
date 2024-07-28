package com.project.how.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.how.R
import com.project.how.adapter.recyclerview.schedule.AiDaysScheduleAdapter
import com.project.how.data_class.recyclerview.AiSchedule
import com.project.how.data_class.recyclerview.DaysSchedule
import com.project.how.data_class.recyclerview.Schedule
import com.project.how.data_class.dto.schedule.DailySchedule
import com.project.how.data_class.dto.country.GetCountryInfoRequest
import com.project.how.data_class.dto.country.GetCountryLocationResponse
import com.project.how.data_class.dto.schedule.AddCheckListsRequestElement
import com.project.how.data_class.dto.schedule.GetFastestSchedulesResponse
import com.project.how.data_class.dto.schedule.GetLatestSchedulesResponse
import com.project.how.data_class.dto.schedule.GetScheduleListResponse
import com.project.how.data_class.dto.schedule.ScheduleDetail
import com.project.how.model.ScheduleRepository
import com.project.how.network.client.ScheduleRetrofit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ScheduleViewModel : ViewModel() {
    private var scheduleRepository : ScheduleRepository = ScheduleRepository()
    private val _nearScheduleDayLiveData = scheduleRepository.nearScheduleDayLiveData
    private val _scheduleLiveData = scheduleRepository.scheduleLiveData
    private val _scheduleListLiveData = scheduleRepository.scheduleListLiveData
    private val _latestScheduleLiveData = scheduleRepository.latestScheduleLiveData
    val nearScheduleDayLiveData : LiveData<GetFastestSchedulesResponse>
        get() = _nearScheduleDayLiveData
    val scheduleLiveData : LiveData<Schedule>
        get() = _scheduleLiveData
    val scheduleListLiveData : LiveData<GetScheduleListResponse>
        get() = _scheduleListLiveData
    val latestScheduleLiveData : LiveData<GetLatestSchedulesResponse>
        get() = _latestScheduleLiveData

    fun getSchedule(schedule : Schedule){
        viewModelScope.launch {
            scheduleRepository.getSchedule(schedule)
        }
    }

    fun getSchedule(aiSchedule : AiSchedule){
        viewModelScope.launch {
            scheduleRepository.getSchedule(aiSchedule).collect{
                getSchedule(it)
            }
        }
    }

    fun getTotalCost(schedule: Schedule) : Flow<Long> = scheduleRepository.getTotalCost(schedule)

    fun getDaysSchedule(context: Context, schedule: Schedule, response : ScheduleDetail) : Flow<Schedule> = scheduleRepository.getDaysSchedule(context, schedule, response)

    fun getEmptyDaysSchedule(startDate: String, endDate: String) : MutableList<MutableList<DaysSchedule>>{
        val std = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        val diff = ChronoUnit.DAYS.between(std, end)
        var dailySchedule = mutableListOf<MutableList<DaysSchedule>>()

        for (i in 0 ..diff){
            dailySchedule.add(mutableListOf<DaysSchedule>())
        }

        return dailySchedule
    }

    fun saveSchedule(context: Context, accessToken: String, schedule: Schedule): Flow<Int> = callbackFlow {
        ScheduleRetrofit.getApiService()?.let { apiService ->
            val callback = object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Log.d("saveSchedule is success", "response code : ${response.code()}")
                        trySend(SUCCESS).isSuccess
                    } else {
                        Log.d("saveSchedule is not success", "response code : ${response.code()}")
                        trySend(NETWORK_FAILED).isSuccess
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("saveSchedule onFailure", "${t.message}")
                    trySend(NETWORK_FAILED).isSuccess
                }
            }
            apiService.saveSchedule(context.getString(R.string.bearer_token, accessToken), changeClassFromScheduleToSaveSchedule(context, schedule))
                .enqueue(callback)
        } ?: close()

        awaitClose()
    }

    fun updateSchedule(context: Context, accessToken: String, id: Long, schedule: Schedule): Flow<Int> = callbackFlow {
        ScheduleRetrofit.getApiService()?.let { apiService ->
            val callback = object : Callback<ScheduleDetail> {
                override fun onResponse(call: Call<ScheduleDetail>, response: Response<ScheduleDetail>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null){
                            Log.d("saveSchedule is success", "response code : ${response.code()}")
                            trySend(SUCCESS)
                        }else{
                            Log.d("updateSchedule is success", "response.body is null")
                            trySend(EMPTY_SCHEDULE)
                        }
                    } else {
                        Log.d("saveSchedule is not success", "response code : ${response.code()}")
                        trySend(NETWORK_FAILED).isSuccess
                    }
                }

                override fun onFailure(call: Call<ScheduleDetail>, t: Throwable) {
                    Log.d("saveSchedule onFailure", "${t.message}")
                    trySend(NETWORK_FAILED).isSuccess
                }
            }
            apiService.updateSchedule(context.getString(R.string.bearer_token, accessToken), id, changeClassFromScheduleToSaveSchedule(context, schedule))
                .enqueue(callback)
        } ?: close()

        awaitClose()
    }

    private fun getScheduleType(context: Context, type : Int): String {
        when(type){
            AiDaysScheduleAdapter.AIRPLANE ->{ return context.getString(R.string.airplane_string) }
            AiDaysScheduleAdapter.HOTEL -> {return context.getString(R.string.hotel_string)}
            AiDaysScheduleAdapter.PLACE -> {return context.getString(R.string.place_string)}
        }
        return context.getString(R.string.place_string)
    }

    private fun changeClassFromScheduleToSaveSchedule(context : Context, schedule: Schedule) : ScheduleDetail {
        val startDate = LocalDate.parse(schedule.startDate, DateTimeFormatter.ISO_DATE)
        var dailySchedules = listOf<DailySchedule>()
        schedule.dailySchedule.forEachIndexed { index, data ->
            var schedules = listOf<com.project.how.data_class.dto.schedule.Schedule>()
            data.forEach {daysSchedule->
                val schedule = com.project.how.data_class.dto.schedule.Schedule(
                    daysSchedule.todo,
                    daysSchedule.places,
                    getScheduleType(context, daysSchedule.type),
                    daysSchedule.cost,
                    daysSchedule.latitude ?: 0.0,
                    daysSchedule.longitude ?: 0.0
                )
                schedules = schedules.plus(schedule)
            }
            val date = startDate.plusDays(index.toLong())
            val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            val ds = DailySchedule(
                date.format(formatter),
                schedules
            )
            dailySchedules = dailySchedules.plus(ds)
        }

        return ScheduleDetail(
            schedule.title,
            schedule.country,
            schedule.startDate,
            schedule.endDate,
            dailySchedules
        )
    }

    fun updateDailySchedule(schedule: Schedule, startDate: String, endDate: String) {
        viewModelScope.launch {
            scheduleRepository.updateDailySchedule(schedule, startDate, endDate)
        }
    }

    fun getScheduleList(context: Context, accessToken: String){
        ScheduleRetrofit.getApiService()!!.getScheduleList(context.getString(R.string.bearer_token, accessToken))
            .enqueue(object : Callback<GetScheduleListResponse>{
                override fun onResponse(
                    call: Call<GetScheduleListResponse>,
                    response: Response<GetScheduleListResponse>
                ) {
                    if (response.isSuccessful){
                        val result = response.body()
                        if (result != null){
                            Log.d("getSchedule is success", "size : ${result.size}\nresponse code : ${response.code()}")
                            scheduleRepository.getScheduleList(result)
                        }else{
                            Log.d("getSchedule is success", "response.body() is null\nresponse code : ${response.code()}")
                        }
                    }else{
                        Log.d("getSchedule is not success", "response code : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GetScheduleListResponse>, t: Throwable) {
                    Log.d("getScheduleList onFailure", "${t.message}")
                }

            })
    }

    fun deleteSchedule(context: Context, accessToken: String, id : Long) = callbackFlow {
        ScheduleRetrofit.getApiService()?.let {apiService ->
            apiService.deleteSchedule(context.getString(R.string.bearer_token, accessToken), id)
            .enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful){
                        Log.d("deleteSchedule is success", "response code : ${response.code()}")
                        trySend(SUCCESS)
                        close()
                    }else{
                        Log.d("deleteSchedule is not success", "response code : ${response.code()}\n${response.errorBody()}")
                        if (response.code() == 404){
                            trySend(NOT_EXIST_SCHEDULE)
                            close()
                        }else if(response.code() == 401){
                            trySend(OTHER_USER_SCHEDULE)
                            close()
                        }

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("deleteSchedule", "${t.message}")
                    trySend(NETWORK_FAILED)
                    close()
                }
            })
        } ?: close()

        awaitClose()
    }

    fun getScheduleDetail(context: Context, accessToken: String, id : Long) = callbackFlow<Int>{
        Log.d("onCreate", "getScheduleDetail start\naccessToken : $accessToken\nid : $id")
        ScheduleRetrofit.getApiService()?.let {apiService ->
            apiService.getScheduleDetail(context.getString(com.project.how.R.string.bearer_token, accessToken), id)
            .enqueue(object : Callback<ScheduleDetail>{
                override fun onResponse(
                    call: Call<ScheduleDetail>,
                    response: Response<ScheduleDetail>
                ) {
                    if (response.isSuccessful){
                        val result = response.body()
                        if(result != null){
                            viewModelScope.launch {
                                Log.d("getScheduleDetail is success", "schedule name : ${result.scheduleName}\ndailySchedules lastIndex : ${result.dailySchedules.lastIndex}")
                                val schedule = Schedule(
                                    result.scheduleName,
                                    result.country,
                                    result.startDate,
                                    result.endDate,
                                    0,
                                    mutableListOf<MutableList<DaysSchedule>>()
                                )
                                if ((result.dailySchedules.isEmpty())){
                                    schedule.dailySchedule = getEmptyDaysSchedule(result.startDate, result.endDate)
                                    getSchedule(schedule)
                                    trySend(SUCCESS)
                                    close()
                                }else{
                                    val setFlow = getDaysSchedule(context, schedule, result)
                                    setFlow.collect{afterSchedule ->
                                        getTotalCost(afterSchedule).collect{totalCost ->
                                            schedule.cost = totalCost
                                            getSchedule(schedule)
                                            trySend(SUCCESS)
                                            close()
                                        }
                                    }
                                }
                            }
                        }else{
                            Log.d("getScheduleDetail is success", "response body is null")
                            trySend(NOT_EXIST_SCHEDULE)
                            close()
                        }
                    }else{
                        Log.d("getScheduleDetail is not success", "response code : ${response.code()}\n${response.errorBody()}")
                        trySend(NOT_EXIST_SCHEDULE)
                        close()
                    }
                }

                override fun onFailure(call: Call<ScheduleDetail>, t: Throwable) {
                    Log.d("getScheduleDetail is failed", "${t.message}")
                    trySend(NETWORK_FAILED)
                }

            })
        } ?: close()

        awaitClose()
    }

    fun getFastestSchedules(context: Context, accessToken: String){
        ScheduleRetrofit.getApiService()!!
            .getFastestSchedule(context.getString(R.string.bearer_token, accessToken))
            .enqueue(object : Callback<GetFastestSchedulesResponse>{
                override fun onResponse(
                    p0: Call<GetFastestSchedulesResponse>,
                    p1: Response<GetFastestSchedulesResponse>
                ) {
                    if (p1.isSuccessful){
                        val result = p1.body()
                        if (result != null){
                            scheduleRepository.getNearScheduleDay(result)
                            Log.d("getFastSchedule", "id : ${result.id}\ndday : ${result.dday}\nname : ${result.scheduleName}")
                        }else{
                            Log.d("getFastestSchedule", "result is null\ncode : ${p1.code()}")
                        }
                    }else{
                        Log.d("getFastestSchedule", "response is failed\ncode : ${p1.code()}")
                    }
                }

                override fun onFailure(p0: Call<GetFastestSchedulesResponse>, p1: Throwable) {
                    Log.d("getFastestSchedule", "getFastestSchedule is failed\n${p1.message}")
                }

            })
    }

    fun getLatestSchedules(context: Context, accessToken: String) : Flow<Int> = callbackFlow {
        ScheduleRetrofit.getApiService()?.let { apiService ->
            apiService.getLatestSchedules(context.getString(R.string.bearer_token, accessToken), LATEST_COUNT)
                .enqueue(object : Callback<GetLatestSchedulesResponse>{
                    override fun onResponse(
                        p0: Call<GetLatestSchedulesResponse>,
                        p1: Response<GetLatestSchedulesResponse>
                    ) {
                        if (p1.isSuccessful){
                            val result = p1.body()
                            if (result != null){
                                Log.d("getLatestSchedule", "result.size : ${result.size}")
                                scheduleRepository.getLatestSchedule(result)
                                trySend(SUCCESS)
                            }else{
                                Log.d("getLatestSchedule", "result is null\ncode :${p1.code()}")
                                trySend(EMPTY_SCHEDULE)
                            }
                        }else{
                            Log.d("getLatestSchedule", "response is failed\ncode : ${p1.code()}")
                            trySend(EMPTY_SCHEDULE)
                        }
                    }

                    override fun onFailure(p0: Call<GetLatestSchedulesResponse>, p1: Throwable) {
                        Log.d("getLatestSchedule", "getLatestSchedule is failed\n${p1.message}")
                        trySend(NETWORK_FAILED)
                    }

                })

        } ?: close()

        awaitClose()
    }


    fun addCheckList(accessToken: String, scheduleId : Long, newCheckList : List<AddCheckListsRequestElement>){

    }

    companion object{
        const val NULL_LOCATIONS = -1
        const val NETWORK_FAILED = -2
        const val NOT_EXIST_SCHEDULE = -3
        const val OTHER_USER_SCHEDULE = -4
        const val EMPTY_SCHEDULE = -5
        const val SUCCESS = 0
        const val LATEST_COUNT : Long = 7
    }
}