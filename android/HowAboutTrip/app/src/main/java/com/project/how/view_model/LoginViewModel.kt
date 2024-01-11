package com.project.how.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.project.how.model.LoginRepository

class LoginViewModel : ViewModel() {
    private var loginRepository : LoginRepository = LoginRepository()
    private val _userLiveData = loginRepository.userLiveData
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData

    fun getUser(idToken: String){
        loginRepository.getUser(idToken)
    }
}