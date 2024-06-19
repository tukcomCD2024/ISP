package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.recyclerview.AiSchedule

class AiScheduleRepository {
    private val _aiScheduleLiveData = MutableLiveData<AiSchedule>()
    private val _aiScheduleListLiveData = MutableLiveData<List<AiSchedule>>()
    val aiScheduleLiveData : LiveData<AiSchedule>
        get() = _aiScheduleLiveData
    val aiScheduleListLiveData : LiveData<List<AiSchedule>>
        get() = _aiScheduleListLiveData

    fun getAiSchedule(data : AiSchedule){
        _aiScheduleLiveData.postValue(data)
    }

    fun getAiScheduleList(data : List<AiSchedule>){
        _aiScheduleListLiveData.postValue(data)
    }
}