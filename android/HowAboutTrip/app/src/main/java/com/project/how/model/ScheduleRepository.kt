package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

class ScheduleRepository {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time.time
    private val _nearScheduleDayLiveData : MutableLiveData<Long> = MutableLiveData()
    val nearScheduleDayLiveData : LiveData<Long>
        get() = _nearScheduleDayLiveData


    fun getDday() : Flow<Long> = flow {
        this.emit(((nearScheduleDayLiveData.value?.minus(today))?.div((24 * 60 * 60 * 1000)) ?: ERROR) as Long)
    }

    fun getNearScheduleDay(day : Long){
        _nearScheduleDayLiveData.postValue(day)
    }companion object{
        const val ERROR = -1
    }
}