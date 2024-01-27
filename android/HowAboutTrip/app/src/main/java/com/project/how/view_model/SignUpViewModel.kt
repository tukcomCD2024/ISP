package com.project.how.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.data_class.dto.SignUpInfo
import com.project.how.model.SignUpRepository

class SignUpViewModel : ViewModel() {
    private var signUpRepository : SignUpRepository = SignUpRepository()
    private val _infoLiveData = signUpRepository.infoLiveData
    val infoLiveData : LiveData<SignUpInfo>
        get() = _infoLiveData

    fun getInfo(name : String, birth : String, phone : String){
        signUpRepository.getInfo(SignUpInfo(name, birth, phone))
    }
}