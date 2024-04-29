package com.project.how.view.activity.ai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.adapter.recyclerview.AiDaysScheduleAdapter
import com.project.how.adapter.recyclerview.AiScheduleAdapter
import com.project.how.data_class.AiScheduleInput
import com.project.how.data_class.recyclerview.AiDaysSchedule
import com.project.how.data_class.recyclerview.AiSchedule
import com.project.how.databinding.ActivityAiScheduleListBinding
import com.project.how.view_model.AiScheduleViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch

class AiScheduleListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAiScheduleListBinding
    private var data = mutableListOf<AiSchedule>()
    private lateinit var adapter : AiScheduleAdapter
    private val scheduleViewModel : ScheduleViewModel by viewModels()
    private val aiScheduleViewModel : AiScheduleViewModel by viewModels() //임시 테스트
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ai_schedule_list)
        binding.ai = this
        binding.lifecycleOwner = this

        aiScheduleViewModel.getTestData()

        aiScheduleViewModel.aiScheduleLiveData.observe(this){
            data.add(it)
            data.add(it)
            adapter = AiScheduleAdapter(this@AiScheduleListActivity, data)
            binding.scheduleList.adapter = adapter
        }
    }
}