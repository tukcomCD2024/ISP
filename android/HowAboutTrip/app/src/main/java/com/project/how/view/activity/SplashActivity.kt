package com.project.how.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.databinding.ActivitySplashBinding
import com.project.how.view_model.LoginViewModel
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.splash = this

        lifecycleScope.launch {
            loginViewModel.init(this@SplashActivity)
            loginViewModel.tokensSaveLiveData.observe(this@SplashActivity){saveCheck->
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                intent.putExtra(resources.getString(R.string.save_check), saveCheck)
                startActivity(intent)
                finish()
            }
        }
    }
}