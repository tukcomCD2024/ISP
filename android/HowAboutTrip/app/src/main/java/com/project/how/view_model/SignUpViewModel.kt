package com.project.how.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.data_class.dto.EmptyResponse
import com.project.how.data_class.dto.SignUpInfo
import com.project.how.data_class.dto.SignUpRequest
import com.project.how.model.SignUpRepository
import com.project.how.network.client.MemberRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {
    private var signUpRepository : SignUpRepository = SignUpRepository()
    private val _infoLiveData = signUpRepository.infoLiveData
    val infoLiveData : LiveData<SignUpInfo>
        get() = _infoLiveData

    fun getInfo(accessToken : String, name : String, birth : String, phone : String){
        val si = SignUpRequest(name, birth, phone)
        MemberRetrofit.getApiService()!!
            .signUp(accessToken, si)
            .enqueue(object : Callback<EmptyResponse>{
                override fun onResponse(
                    call: Call<EmptyResponse>,
                    response: Response<EmptyResponse>
                ) {
                    if (response.isSuccessful){
                        signUpRepository.getInfo(SignUpInfo(name, birth, phone))
                        Log.d("getTokens success", "code : ${response.code()}")
                    }else{
                        Log.d("getInfo response is not success", "code : ${response.code()}\nError : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<EmptyResponse>, t: Throwable) {
                    Log.d("getInfo onFailure", "${t.message}")
                }

            })
    }
}