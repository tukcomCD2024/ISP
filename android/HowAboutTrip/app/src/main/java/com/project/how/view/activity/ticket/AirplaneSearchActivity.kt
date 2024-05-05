package com.project.how.view.activity.ticket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.ActivityAirplaneSearchBinding

class AirplaneSearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAirplaneSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_airplane_search)
        binding.search = this
        binding.lifecycleOwner = this

    }
}