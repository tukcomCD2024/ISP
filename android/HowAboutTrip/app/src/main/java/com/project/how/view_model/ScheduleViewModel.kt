package com.project.how.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.model.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class ScheduleViewModel : ViewModel() {
    private var scheduleRepository : ScheduleRepository = ScheduleRepository()
    private val _nearScheduleDayLiveData = scheduleRepository.nearScheduleDayLiveData
    val nearScheduleDayLiveData : LiveData<Long>
        get() = _nearScheduleDayLiveData

    fun getDday() : Flow<Long> = scheduleRepository.getDday()

    fun getNearScheduleDay(day : Long){
        scheduleRepository.getNearScheduleDay(day)
    }

}