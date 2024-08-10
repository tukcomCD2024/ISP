package com.project.how.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.project.how.R
import com.project.how.data_class.MemberInfo
import com.project.how.data_class.dto.LoginRequest
import com.project.how.data_class.Tokens
import com.project.how.data_class.dto.AuthRecreateRequest
import com.project.how.data_class.dto.EmptyResponse
import com.project.how.data_class.dto.GetInfoResponse
import com.project.how.data_class.dto.SignUpRequest
import com.project.how.model.MemberRepository
import com.project.how.network.client.MemberRetrofit
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MemberViewModel : ViewModel() {
    private var memberRepository : MemberRepository = MemberRepository()
    private val _currentUserLiveData = memberRepository.currentUserLiveData
    private val _userLiveData = memberRepository.userLiveData
    private val _tokensLiveData = memberRepository.tokensLiveData
    private val _tokensSaveLiveData = memberRepository.tokensSaveLiveData
    private val _infoLiveData = memberRepository.memberInfoLiveData
    val currentUserLiveData : LiveData<FirebaseUser>
        get() = _currentUserLiveData
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData
    val tokensLiveData : LiveData<Tokens>
        get() = _tokensLiveData
    val tokensSaveLiveData : LiveData<Boolean>
        get() = _tokensSaveLiveData
    val memberInfoLiveData : LiveData<MemberInfo>
        get() = _infoLiveData

    private var authRecreateCount = 0

    init {
        Log.d("LoginViewModel init", "checkCurrentUser start")
        memberRepository.checkCurrentUser()
        Log.d("LoginViewModel init", "checkCurrentUser end")
        authRecreateCount = 0
    }

    fun getUser(idToken: String) {
        memberRepository.getUser(idToken)
    }

    suspend fun init(context: Context){
        Log.d("init", "LoginViewModel init start")
        memberRepository.init(context)
    }

    fun authRecreate(context: Context, arr: AuthRecreateRequest) {
        MemberRetrofit.getApiService()!!
            .authRecreate(arr)
            .enqueue(object : Callback<EmptyResponse>{
                override fun onResponse(
                    call: Call<EmptyResponse>,
                    response: Response<EmptyResponse>
                ) {
                    viewModelScope.launch {
                        val result = response.body().toString()
                        val accessToken = response.headers()[ACCESS_TOKEN]
                        val refreshToken = response.headers()[REFRESH_TOKEN]
                        Log.d(
                            "authRecreate success",
                            "code : ${response.code()}\nresult : ${result}\naccessToken : ${accessToken}\nrefreshToken : $refreshToken"
                        )
                        memberRepository.getTokens(context, accessToken!!, refreshToken!!)
                    }
                }

                override fun onFailure(call: Call<EmptyResponse>, t: Throwable) {
                    Log.d("authRecreate onFailure", "${t.message}")
                }

            })
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
                            memberRepository.getTokens(context, accessToken!!, refreshToken!!)
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

    fun getInfoSignUp(context : Context, accessToken : String, name : String, birth : String, phone : String){
        val si = SignUpRequest(name, birth, phone)
        MemberRetrofit.getApiService()!!
            .signUp(context.getString(R.string.bearer_token, accessToken), si)
            .enqueue(object : Callback<EmptyResponse>{
                override fun onResponse(
                    call: Call<EmptyResponse>,
                    response: Response<EmptyResponse>
                ) {
                    if (response.isSuccessful){
                        memberRepository.getInfo(MemberInfo(name, birth, phone))
                        Log.d("getInfoSignUp success", "code : ${response.code()}")
                    }else{
                        Log.d("getInfoSignUp response is not success", "code : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<EmptyResponse>, t: Throwable) {
                    Log.d("getInfoSignUp onFailure", "${t.message}")
                }
            })
    }

    fun getInfo(context: Context, accessToken: String){
        MemberRetrofit.getApiService()!!
            .getInfo(context.resources.getString(R.string.bearer_token, accessToken))
            .enqueue(object  : Callback<GetInfoResponse>{
                override fun onResponse(
                    call: Call<GetInfoResponse>,
                    response: Response<GetInfoResponse>
                ) {
                    if(response.isSuccessful){
                        val result = response.body()
                        if(result != null){
                            val name = result.name
                            val birth = result.birth
                            val phone = result.phone
                            memberRepository.getInfo(MemberInfo(name, birth, phone))
                            Log.d("getInfo success", "code : ${response.code()}\nname : ${name}\nbirth : ${birth}\nphone : $phone")
                        }else{
                            Log.d("getInfo result is null", "code : ${response.code()}\n message : ${response.message()}")
                        }
                    }else if(response.code() == BAD_REQUEST){
                        if (authRecreateCount<2){
                            tokensLiveData.value
                                ?.let { AuthRecreateRequest(it.refreshToken) }
                                ?.let { refreshToken->
                                    authRecreate(context, refreshToken)
                                    authRecreateCount++}
                            Log.d("getInfo Bad Request", "code : ${response.code()}\nExecute authRecreate $authRecreateCount")
                        }
                    }else{
                        Log.d("getInfo response is not success", "code : ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<GetInfoResponse>, t: Throwable) {
                    Log.d("getInfo onFailure", "${t.message}")
                }
            })
    }

    companion object{
        private const val ON_FAILURE = 99999
        private const val ACCESS_TOKEN = "Access-Token"
        private const val REFRESH_TOKEN = "Refresh-Token"
        private const val BAD_REQUEST = 400
    }
}