package com.project.how.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.ActivityAddAicalendarBinding

class AddAICalendarActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddAicalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_aicalendar)
        binding.ai = this
        binding.lifecycleOwner = this
    }
}