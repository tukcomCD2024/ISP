package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.recyclerview.AiSchedule

class AiScheduleRepository {
    private val _aiScheduleLiveData = MutableLiveData<AiSchedule>()
    val aiScheduleLiveData : LiveData<AiSchedule>
        get() = _aiScheduleLiveData

    fun getAiSchedule(data : AiSchedule){
        _aiScheduleLiveData.postValue(data)
    }
}