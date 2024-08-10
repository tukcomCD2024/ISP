package com.project.how.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.databinding.ActivitySplashBinding
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private val memberViewModel: MemberViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.splash = this

        lifecycleScope.launch {
            memberViewModel.init(this@SplashActivity)
        }

        memberViewModel.tokensSaveLiveData.observe(this@SplashActivity){saveCheck->
            Log.d("tokensSaveLiveData observe", "start\nsaveCheck : $saveCheck")
            if (saveCheck){
                getMemberInfo()
                memberViewModel.memberInfoLiveData.observe(this){
                    Log.d("tokenSaveLiveData observe", "memberInfoLiveData\nname : ${it.name}\nphone : ${it.phone}\nbirth : ${it.birth}")
                    moveMain()
                }
            }else{
                moveLogin()
            }
        }
    }

    private fun getMemberInfo() {
        memberViewModel.tokensLiveData.value?.let { memberViewModel.getInfo(this, it.accessToken) }
    }

    private fun moveLogin(){
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveMain(){
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}