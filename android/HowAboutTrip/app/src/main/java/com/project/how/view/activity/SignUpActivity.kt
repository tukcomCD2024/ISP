package com.project.how.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.ActivitySignUpBinding
import com.project.how.view_model.MemberViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private val memberViewModel : MemberViewModel by viewModels()
    private lateinit var days : List<Int>
    private var selectedDay : LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.signUp = this
        binding.lifecycleOwner = this

        binding.phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    fun sendInfo(){
        val name = binding.name.text.toString()
        val phone = binding.phone.text.toString()
        val birth = binding.birth.text.toString()

        Log.d("sendInfo", "IF Before\nname : $name\t${name.length}\nphone : $phone\t${phone.length}\nbirth : $birth\t${birth.length}")

        if(name.isNotEmpty() && phone.length == 13 && birth.length == 8){
            val n = name.trim()
            val p = phone
            val bDate = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyyMMdd"))
            val b = bDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            Log.d("sendInfo", "IF After\nname : $n\t${n.length}\nphone : $p\t${p.length}\nbirth : $b\t${b .length}")
            memberViewModel.tokensLiveData.value?.let { memberViewModel.getInfoSignUp(this, it.accessToken, n, b, p) }
            memberViewModel.memberInfoLiveData.observe(this){ info ->
                moveMainActivity()
            }
        }else{
            Toast.makeText(this, "[필수] 모든 항목은 필수로 입력하셔야 합니다.\n모든 항목이 정해진 형식에 맞춰서 작성되었는지 확인하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Toast.makeText(this, "[필수] 모든 항목은 필수로 입력하셔야 합니다.\n한 번 더 누르시면 앱이 꺼지면 기본값으로 설정됩니다.\n기본값으로 설정될 시 다음 접속 시에 )", Toast.LENGTH_SHORT).show()
    }
}