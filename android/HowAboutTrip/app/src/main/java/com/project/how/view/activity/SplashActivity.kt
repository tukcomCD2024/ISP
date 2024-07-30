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
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private val memberViewModel: MemberViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.splash = this

        Log.d("Splash Activity", "start")

        memberViewModel.memberInfoLiveData.observe(this){
            Log.d("tokenSaveLiveData observe", "memberInfoLiveData\nname : ${it.name}\nphone : ${it.phone}\nbirth : ${it.birth}")
            if (it.name == getString(R.string.error)){
                Toast.makeText(this, "이전의 회원가입 과정이 정상적으로 진행되지 않았습니다.\n모든 정보를 기입해주세요.", Toast.LENGTH_SHORT).show()
                moveSignUp()
            }else{
                moveMain()
            }
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
                memberViewModel.getInfo(this@SplashActivity).collect{ check->
                    if (check != MemberViewModel.SUCCESS){
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

    private fun moveSignUp(){
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}