package com.project.how.view.activity.ticket

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.adapter.recyclerview.AirplaneListAdapter
import com.project.how.databinding.ActivityAirplaneListBinding
import kotlinx.coroutines.launch

class AirplaneListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAirplaneListBinding
    private lateinit var adapter : AirplaneListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_airplane_list)
        binding.airport = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            MobileAds.initialize(this@AirplaneListActivity)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }
    }


}