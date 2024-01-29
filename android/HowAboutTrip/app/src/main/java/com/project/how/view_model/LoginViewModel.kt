package com.project.how.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.project.how.data_class.dto.LoginRequest
import com.project.how.data_class.Tokens
import com.project.how.model.LoginRepository
import com.project.how.network.client.MemberRetrofit
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginViewModel : ViewModel() {
    private var loginRepository : LoginRepository = LoginRepository()
    private val _currentUserLiveData = loginRepository.currentUserLiveData
    private val _userLiveData = loginRepository.userLiveData
    private val _tokensLiveData = loginRepository.tokensLiveData
    private val _tokensSaveLiveData = loginRepository.tokensSaveLiveData
    val currentUserLiveData : LiveData<FirebaseUser>
        get() = _currentUserLiveData
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData
    val tokensLiveData : LiveData<Tokens>
        get() = _tokensLiveData
    val tokensSaveLiveData : LiveData<Boolean>
        get() = _tokensSaveLiveData

    init {
        Log.d("LoginViewModel init", "checkCurrentUser start")
        loginRepository.checkCurrentUser()
        Log.d("LoginViewModel init", "checkCurrentUser end")
    }

    fun getUser(idToken: String) {
        loginRepository.getUser(idToken)
    }

    fun init(context: Context){
        viewModelScope.launch {
            loginRepository.init(context)
        }
    }

    suspend fun getTokens(context : Context, lr: LoginRequest) : Int = suspendCoroutine{ continuation ->
        Log.d("getTokens", "loginRequest : ${lr.uid}")
        MemberRetrofit.getApiService()!!
            .login(lr)
            .enqueue(object : Callback<String>{
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        viewModelScope.launch {
                            val result = response.body().toString()
                            val accessToken = response.headers()[ACCESS_TOKEN]
                            val refreshToken = response.headers()[REFRESH_TOKEN]
                            Log.d("getTokens success", "code : ${response.code()}\nresult : ${result}\naccessToken : ${accessToken}\nrefreshToken : $refreshToken")
                            loginRepository.getTokens(context, accessToken!!, refreshToken!!)
                            continuation.resume(response.code())
                        }
                    } else {
                        Log.d("getTokens response is not success", "code : ${response.code()}\nError : ${response.code()}")
                        continuation.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("getTokens onFailure", "${t.message}")
                    continuation.resume(ON_FAILURE)
                }
            })
    }
    companion object{
        private const val ON_FAILURE = 99999
        private const val ACCESS_TOKEN = "Access-Token"
        private const val REFRESH_TOKEN = "Refresh-Token"
    }
}