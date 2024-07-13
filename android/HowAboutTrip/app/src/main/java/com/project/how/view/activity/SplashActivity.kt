package com.project.how.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

        memberViewModel.memberInfoLiveData.observe(this){
            Log.d("tokenSaveLiveData observe", "memberInfoLiveData\nname : ${it.name}\nphone : ${it.phone}\nbirth : ${it.birth}")
            moveMain()
        }

        lifecycleScope.launch {
            memberViewModel.init(this@SplashActivity)
        }

        memberViewModel.tokensSaveLiveData.observe(this@SplashActivity){saveCheck->
            Log.d("tokensSaveLiveData observe", "start\nsaveCheck : $saveCheck")
            if (saveCheck){
                getMemberInfo()
            }else{
                moveLogin()
            }
        }
    }

    private fun getMemberInfo() {
        memberViewModel.tokensLiveData.value?.let {
            lifecycleScope.launch {
                memberViewModel.getInfo(this@SplashActivity, it.accessToken).collect{ check->
                    if (check == MemberViewModel.SUCCESS){
                        moveMain()
                    }else{
                        if (check == MemberViewModel.ON_FAILURE){
                            Toast.makeText(this@SplashActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                        }
                        moveLogin()
                    }
                }
            }
        }
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