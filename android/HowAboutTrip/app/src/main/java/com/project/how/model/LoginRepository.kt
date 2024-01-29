package com.project.how.model

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.project.how.data_class.Tokens
import com.project.how.datastore.TokenDataStore

class LoginRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _currentUserLiveData = MutableLiveData<FirebaseUser>()
    private val _userLiveData = MutableLiveData<FirebaseUser>()
    private val _tokensLiveData = MutableLiveData<Tokens>()
    private val _tokensSaveLiveData = MutableLiveData<Boolean>()
    val currentUserLiveData : LiveData<FirebaseUser>
        get() = _currentUserLiveData
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData
    val tokensLiveData : LiveData<Tokens>
        get() = _tokensLiveData
    val tokensSaveLiveData : LiveData<Boolean>
        get() = _tokensSaveLiveData

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
        TokenDataStore.getTokens(context).collect{
            if (it != null){
                _tokensLiveData.postValue(it)
                _tokensSaveLiveData.postValue(true)
            }else{
                _tokensSaveLiveData.postValue(false)
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

    suspend fun getTokens(context: Context, accessToken : String, refreshToken: String) {
        val tokens = Tokens(accessToken, refreshToken)
        _tokensLiveData.postValue(tokens)
        TokenDataStore.saveTokens(context, Tokens(accessToken, refreshToken))
    }
}