package com.project.how.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.how.R
import com.project.how.data_class.AiSchedule
import com.project.how.data_class.Schedule
import com.project.how.data_class.dto.DailySchedule
import com.project.how.data_class.dto.ScheduleDetail
import com.project.how.model.ScheduleRepository
import com.project.how.network.client.ScheduleRetrofit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScheduleViewModel : ViewModel() {
    private var scheduleRepository : ScheduleRepository = ScheduleRepository()
    private val _nearScheduleDayLiveData = scheduleRepository.nearScheduleDayLiveData
    private val _scheduleLiveData = scheduleRepository.scheduleLiveData
    val nearScheduleDayLiveData : LiveData<Long>
        get() = _nearScheduleDayLiveData
    val scheduleLiveData : LiveData<Schedule>
        get() = _scheduleLiveData

    fun getDday() : Flow<Long> = scheduleRepository.getDday()

    fun getNearScheduleDay(day : Long){
        scheduleRepository.getNearScheduleDay(day)
    }

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

    fun saveSchedule(context: Context, accessToken: String, schedule: Schedule): Flow<Int> = callbackFlow {
        // checkLocations의 결과를 받을 Flow 생성
        val locationsFlow = checkLocations(schedule)

        // checkLocations의 결과를 기다림
        val check = locationsFlow.first()

        if (check) {
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
                apiService.saveSchedule(context.getString(R.string.bearer_token, accessToken), changeClassFromScheduleToSaveSchedule(schedule))
                    .enqueue(callback)
            } ?: close() // 만약 apiService가 null이면 flow를 종료합니다.
        } else {
            trySend(NULL_LOCATIONS).isSuccess
        }

        awaitClose()
    }

    private fun changeClassFromScheduleToSaveSchedule(schedule: Schedule) : ScheduleDetail{
        val startDate = LocalDate.parse(schedule.startDate, DateTimeFormatter.ISO_DATE)
        var dailySchedules = listOf<DailySchedule>()
        schedule.dailySchedule.forEachIndexed { index, data ->
            var schedules = listOf<com.project.how.data_class.dto.Schedule>()
            data.forEach {daysSchedule->
                val schedule = com.project.how.data_class.dto.Schedule(
                    daysSchedule.todo,
                    daysSchedule.places,
                    daysSchedule.cost,
                    daysSchedule.latitude ?: 0.0,
                    daysSchedule.longitude ?: 0.0)
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

    private fun checkLocations(schedule: Schedule) : Flow<Boolean> = flow {
        for (i in schedule.dailySchedule.indices){
            for (j in schedule.dailySchedule[i].indices){
                if ((schedule.dailySchedule[i][j].longitude == null) || (schedule.dailySchedule[i][j].latitude == null))
                    this.emit(false)
            }
        }
        this.emit(true)
    }

    fun updateDailySchedule(schedule: Schedule, startDate: String, endDate: String) {
        viewModelScope.launch {
            scheduleRepository.updateDailySchedule(schedule, startDate, endDate)
        }
    }

    companion object{
        const val NULL_LOCATIONS = -1
        const val NETWORK_FAILED = -2
        const val SUCCESS = 0
    }
}