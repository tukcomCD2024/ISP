package com.project.how.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.data_class.dto.SignUpInfo

class SignUpRepository {
    private val _infoLiveData = MutableLiveData<SignUpInfo>()
    val infoLiveData : LiveData<SignUpInfo>
        get() = _infoLiveData

    fun getInfo(info : SignUpInfo){
        _infoLiveData.postValue(info)
    }
}