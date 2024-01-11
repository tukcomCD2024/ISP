package com.project.how.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _userLiveData = MutableLiveData<FirebaseUser>()
    val userLiveData: LiveData<FirebaseUser>
        get() = _userLiveData

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
}