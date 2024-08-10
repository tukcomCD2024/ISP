package com.project.how.view.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.dto.LoginRequest
import com.project.how.databinding.ActivityLoginBinding
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel: MemberViewModel by viewModels()
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var googleSignInRequest: GoogleSignInClient
    private lateinit var gso : GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.login = this
        binding.lifecycleOwner = this

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_SERVER_ID)
            .requestEmail()
            .build()
        googleSignInRequest = GoogleSignIn.getClient(this, gso)
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.result
                    viewModel.getUser(account.idToken!!)
                    viewModel.userLiveData.observe(this){user->
                        Log.d("activityResultLauncher", "Login Success\nidToken : ${account.idToken}\nid : ${account.id}\nemail : ${account.email}\nuid : ${user.uid}")
                        sendUid(user.uid)
                    }
                } catch (e: Exception){
                    Log.e("activityResultLauncher", "Login Failed\nError : ${e.stackTrace}\n${e.message}")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.currentUserLiveData.observe(this) { user ->
            if (user != null) {
                Log.d("onStart", "viewModel.currentUserLiveData.value != null")
                autoLogin()
            } else {
                Log.d("onStart", "viewModel.currentUserLiveData.value == null")
            }
        }
    }

    private fun sendUid(uid : String){
        lifecycleScope.launch {
            val loginRequest = LoginRequest(uid)
            val code = viewModel.getTokens(this@LoginActivity, loginRequest)
            if(code == EXISTING_MEMBER){
                Log.d("sendUid", "Existing member")
                moveMain()
            }else if(code == NEW_MEMBER){
                Log.d("sendUid", "New member")
                moveSignUp()
            }else{
                Log.e("sendUid", "Login failed")
                Toast.makeText(this@LoginActivity, "[로그인 실패] HowAboutTrip 로그인에 실패했습니다.\n해당 오류가 지속될 경우 문의하시기를 바랍니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autoLogin() {
        googleSignInRequest.silentSignIn().addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                try {
                    val account = task.result
                    viewModel.getUser(account.idToken!!)
                    viewModel.userLiveData.observe(this){user->
                        Log.d("autoLogin", "Auto Login Success\nidToken : ${account.idToken}\nid : ${account.id}\nemail : ${account.email}\nuid : ${user.uid}")
                        sendUid(user.uid)
                    }
                } catch (e: Exception) {
                    Log.e("autoLogin", "Auto Login Failed\nError : ${e.stackTrace}\n${e.message}")
                }
            } else{
                Log.e("autoLogin", "Auto Login Failed\nError : ${task.exception}")
            }
        }
    }
    fun login(){
        activityResultLauncher.launch(googleSignInRequest.signInIntent)
        binding.lottie.pauseAnimation()
    }

    private fun moveSignUp(){
        viewModel.tokensLiveData.observe(this){
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun moveMain(){
        viewModel.tokensLiveData.observe(this){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    companion object{
        const val EXISTING_MEMBER = 200
        const val NEW_MEMBER = 201
    }
}