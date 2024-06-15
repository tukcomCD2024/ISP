package com.project.how.view.activity.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.adapter.recyclerview.AiDaysScheduleAdapter
import com.project.how.adapter.recyclerview.AiScheduleAdapter
import com.project.how.data_class.AiScheduleInput
import com.project.how.data_class.dto.GetCountryLocationResponse
import com.project.how.data_class.recyclerview.AiDaysSchedule
import com.project.how.data_class.recyclerview.AiSchedule
import com.project.how.data_class.recyclerview.AiScheduleList
import com.project.how.databinding.ActivityAiScheduleListBinding
import com.project.how.view.activity.calendar.CalendarEditActivity
import com.project.how.view_model.AiScheduleViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch

class AiScheduleListActivity : AppCompatActivity(), AiScheduleAdapter.OnClickListener {
    private lateinit var binding : ActivityAiScheduleListBinding
    private var data = mutableListOf<AiSchedule>()
    private var lat = 0.0
    private var lng = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ai_schedule_list)
        binding.ai = this
        binding.lifecycleOwner = this

        lat = intent.getDoubleExtra(getString(R.string.server_calendar_latitude), 0.0)
        lng = intent.getDoubleExtra(getString(R.string.server_calendar_longitude), 0.0)
        Log.d("AiScheduleListActivity", "lat : $lat\tlng : $lng\ndata size : ${data.size}")
        data = CalendarEditActivity.getSerializable(this, getString(R.string.aischedule), AiScheduleList::class.java).aiSchedules.toMutableList()

        binding.scheduleList.adapter = AiScheduleAdapter(this, data, this)


    }

    override fun onCreateButtonClicker(data: AiSchedule) {
        val intent = Intent(this, CalendarEditActivity::class.java)
        intent.putExtra(getString(R.string.type), CalendarEditActivity.AI_SCHEDULE)
        intent.putExtra(getString(R.string.aischedule), data)
        intent.putExtra(getString(R.string.server_calendar_latitude), lat)
        intent.putExtra(getString(R.string.server_calendar_longitude), lng)
        startActivity(intent)
    }
}