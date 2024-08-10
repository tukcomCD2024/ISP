package com.project.how.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.how.R
import com.project.how.adapter.CalendarAdapter
import com.project.how.data_class.dto.AuthRecreateRequest
import com.project.how.databinding.ActivitySignUpBinding
import com.project.how.databinding.CalendarBottomSheetBinding
import com.project.how.view_model.CalendarViewModel
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SignUpActivity : AppCompatActivity(), CalendarAdapter.OnItemClickListener {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var calendarBinding : CalendarBottomSheetBinding
    private val calenderViewModel : CalendarViewModel by viewModels()
    private val memberViewModel : MemberViewModel by viewModels()
    private lateinit var days : List<Int>
    private lateinit var adapter : CalendarAdapter
    private var selectedDay : LocalDate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        calendarBinding = CalendarBottomSheetBinding.inflate(layoutInflater)
        binding.signUp = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            memberViewModel.init(this@SignUpActivity)
        }

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
            memberViewModel.tokensLiveData.value?.let { memberViewModel.getInfoSignUp(this, it.accessToken, n, p, b) }
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

//    test용 코드
//    fun authRecreate(){
//        memberViewModel.tokensLiveData.value?.let {
//            memberViewModel.authRecreate(this, AuthRecreateRequest(it.refreshToken))
//        }
//    }

//    캘린더 테스트용 코드
//    fun setCalenderBottomSheetView() {
//        initializeCalendarBottomSheet()
//        observeCalendarData()
//    }
//
//    private fun initializeCalendarBottomSheet() {
//        calendarBinding = CalendarBottomSheetBinding.inflate(layoutInflater)
//        val bottomSheetDialog = BottomSheetDialog(this)
//        bottomSheetDialog.setContentView(calendarBinding.root)
//        bottomSheetDialog.show()
//
//
//        calenderViewModel.selectedDate.observe(this@SignUpActivity) { sd ->
//            Log.d("selectedData observe", "initializeCalendarBottom() 시작")
//            days = listOf()
//            adapter = CalendarAdapter(days, sd, this)
//            calendarBinding.days.layoutManager = GridLayoutManager(this, 7)
//            calendarBinding.days.adapter = adapter
//        }
//
//        calendarBinding.left.setOnClickListener{ minusMonth() }
//        calendarBinding.right.setOnClickListener { plusMonth() }
//        calendarBinding.reset.setOnClickListener { resetDay() }
//        calendarBinding.confirm.setOnClickListener {  }
//    }
//
//    private fun observeCalendarData() {
//        lifecycleScope.launch {
//            calenderViewModel.init().collect { updatedDays ->
//                calenderViewModel.selectedDate.observe(this@SignUpActivity) { sd ->
//                    Log.d("selectedData observe", "observeCalendarData() 시작")
//                    adapter.update(updatedDays, sd)
//                    calendarBinding.month.text = sd.month.value.toString()
//                    calendarBinding.year.text = sd.year.toString()
//                }
//            }
//        }
//    }
//
//    private fun minusMonth(){
//        lifecycleScope.launch {
//            calenderViewModel.minusSelectedDate().collect { updatedDays ->
//                calenderViewModel.selectedDate.observe(this@SignUpActivity) { sd ->
//                    Log.d("selectedData observe", "left.setOnClickListener() 시작")
//                    adapter.update(updatedDays, sd)
//                }
//            }
//        }
//    }
//
//
//    private fun resetDay() {
//        selectedDay = null
//        adapter.resetSelected()
//    }
//
//    private fun plusMonth(){
//        lifecycleScope.launch {
//            calenderViewModel.plusSelectedDate().collect { updatedDays ->
//                calenderViewModel.selectedDate.observe(this@SignUpActivity) { sd ->
//                    Log.d("selectedData observe", "right.setOnClickListener() 시작")
//                    adapter.update(updatedDays, sd)
//                }
//            }
//        }
//    }
//
//    override fun onItemClickListener(data: Int, selected: MutableList<Boolean>, position: Int, sd : LocalDate) {
//        adapter.resetSelected()
//        selected[position] = !selected[position]
//        selectedDay = sd
//    }

}