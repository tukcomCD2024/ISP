package com.project.how.view.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.databinding.ActivityLoginBinding
import com.project.how.view_model.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var googleSignInRequest: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.login = this
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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
                    Log.d("activityResultLauncher", "Login Success\nidToken : ${account.idToken}\nid : ${account.id}\nemail : ${account.email}")
                    startActivity(Intent(this, MainActivity::class.java))
                } catch (e: Exception){
                    Log.e("activityResultLauncher", "Login Failed\nError : ${e.stackTrace}\n${e.message}")
                }
            }
        }

        binding.googleLogin.setOnClickListener {
            activityResultLauncher.launch(googleSignInRequest.signInIntent)
            binding.lottie.pauseAnimation()
        }
    }
}