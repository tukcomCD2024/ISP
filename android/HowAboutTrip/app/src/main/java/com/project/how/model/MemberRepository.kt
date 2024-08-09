package com.project.how.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.project.how.data_class.MemberInfo
import com.project.how.data_class.Tokens
import com.project.how.data_store.TokenDataStore

object MemberRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _currentUserLiveData = MutableLiveData<FirebaseUser>()
    private val _userLiveData = MutableLiveData<FirebaseUser>()
    private val _tokensLiveData = MutableLiveData<Tokens>()
    private val _tokensSaveLiveData = MutableLiveData<Boolean>()
    private val _Member_infoLiveData = MutableLiveData<MemberInfo>()
    val currentUserLiveData : LiveData<FirebaseUser>
        get() = _currentUserLiveData
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData
    val tokensLiveData : LiveData<Tokens>
        get() = _tokensLiveData
    val tokensSaveLiveData : LiveData<Boolean>
        get() = _tokensSaveLiveData
    val memberInfoLiveData : LiveData<MemberInfo>
        get() = _Member_infoLiveData

    fun getUser(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                _userLiveData.postValue(firebaseAuth.currentUser)
            } else{
                Log.e("GoogleAuthRepository", "getUser is failed\n${it.exception}")
            }
        }
    }

    suspend fun init(context: Context) {
        Log.d("init", "LoginRepository init start")
        TokenDataStore.getTokens(context).collect{
            Log.d("init", "LoginRepository init getTokens() \tToken is null : ${it == null}")
            if (it != null){
                Log.d("init", "LoginRepository init getTokens()\naccessToken : ${it.accessToken}\nrefreshToken : ${it.refreshToken}")
                _tokensLiveData.postValue(it)
                _tokensSaveLiveData.postValue(true)
            }else{
                Log.d("init", "LoginRepository init getTokens() _tokensSaveLiveData.postValue(false)")
                _tokensSaveLiveData.postValue(false)
            }
        }
        Log.d("init", "LoginRepository init end")
    }

    fun checkCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            Log.d("checkCurrentUser", "firebaseAuth.current != null\nemail : ${currentUser.email}\ndisplayName : ${currentUser.displayName}")
            _currentUserLiveData.postValue(currentUser)
            Log.d("checkCurrentUser", "currentUserLiveData.value : ${currentUserLiveData.value}")
        }else{
            Log.d("checkCurrentUser", "firebaseAuth.current == null")
        }
    }

    suspend fun getTokens(context: Context, accessToken : String, refreshToken: String) {
        val tokens = Tokens(accessToken, refreshToken)
        _tokensLiveData.postValue(tokens)
        TokenDataStore.saveTokens(context, Tokens(accessToken, refreshToken))
    }

    fun getInfo(memberInfo : MemberInfo){
        _Member_infoLiveData.postValue(memberInfo)
    }
}