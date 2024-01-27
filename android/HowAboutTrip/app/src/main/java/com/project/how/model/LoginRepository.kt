package com.project.how.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.project.how.data_class.Tokens

class LoginRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _currentUserLiveData = MutableLiveData<FirebaseUser>()
    private val _userLiveData = MutableLiveData<FirebaseUser>()
    private val _tokensLiveData = MutableLiveData<Tokens>()
    val currentUserLiveData : LiveData<FirebaseUser>
        get() = _currentUserLiveData
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData
    val tokensLiveData : LiveData<Tokens>
        get() = _tokensLiveData

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
    fun checkCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            Log.d("checkCurrentUser", "firebaseAuth.current != null\nemail : ${currentUser.email}\ndisplayName : ${currentUser.displayName}")
            _currentUserLiveData.postValue(currentUser!!)
            Log.d("checkCurrentUser", "_currentUserLiveData.value : ${_currentUserLiveData.value}")
            Log.d("checkCurrentUser", "currentUserLiveData.value : ${currentUserLiveData.value}")
        }else{
            Log.d("checkCurrentUser", "firebaseAuth.current == null")
        }
    }

    fun getTokens(accessToken : String, refreshToken: String) {
        val tokens = Tokens(accessToken, refreshToken)
        _tokensLiveData.postValue(tokens)
    }
}