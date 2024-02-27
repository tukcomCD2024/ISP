package com.project.how.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.how.data_class.AiSchedule
import com.project.how.data_class.Schedule
import com.project.how.model.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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
        scheduleRepository.getSchedule(schedule)
    }

    fun getSchedule(aiSchedule : AiSchedule){
        viewModelScope.launch {
            scheduleRepository.getSchedule(aiSchedule).collect{
                getSchedule(it)
            }
        }
    }
}