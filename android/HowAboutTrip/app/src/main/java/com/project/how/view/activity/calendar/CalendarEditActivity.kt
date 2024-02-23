package com.project.how.view.activity.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.ActivityCalendarEditBinding

class CalendarEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCalendarEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_edit)
        binding.edit = this
        binding.lifecycleOwner = this
    }
}