package com.project.how.view.activity.ai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.adapter.recyclerview.AiDaysScheduleAdapter
import com.project.how.adapter.recyclerview.AiScheduleAdapter
import com.project.how.data_class.AiDaysSchedule
import com.project.how.data_class.AiSchedule
import com.project.how.databinding.ActivityAiScheduleListBinding
import kotlinx.coroutines.launch

//안씀
class AiScheduleListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAiScheduleListBinding
    private val data = mutableListOf<AiSchedule>()
    private lateinit var adapter : AiScheduleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ai_schedule_list)
        binding.ai = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            val testAiDaysSchedule = listOf<AiDaysSchedule>(AiDaysSchedule(AiDaysScheduleAdapter.PLACE, "text Todo", "text"))
            val dailySchedule = mutableListOf<List<AiDaysSchedule>>()
            for (i in 0..5){
                dailySchedule.add(testAiDaysSchedule)
            }

            data.add(AiSchedule("RecyclerView Text", listOf("#text1", "#text2", "#text3"),
                "https://img.freepik.com/free-photo/vertical-shot-beautiful-eiffel-tower-captured-paris-france_181624-45445.jpg?w=740&t=st=1708260600~exp=1708261200~hmac=01d8abec61f555d0edb040d41ce8ea39904853aea6df7c37ce0b5a35e07c1954",
                "2024-02-18", "2024-02-19", dailySchedule))

            adapter = AiScheduleAdapter(data)

            binding.scheduleList.adapter = adapter
        }
    }
}