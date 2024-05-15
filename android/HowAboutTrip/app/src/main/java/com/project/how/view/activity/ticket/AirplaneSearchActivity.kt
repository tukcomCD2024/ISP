package com.project.how.view.activity.ticket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayoutMediator
import com.project.how.R
import com.project.how.adapter.viewpager.ViewPagerAdapter
import com.project.how.databinding.ActivityAirplaneSearchBinding
import com.project.how.view.fragment.ticket.OneWaySearchFragment
import com.project.how.view.fragment.ticket.RoundTripSearchFragment
import kotlinx.coroutines.launch

class AirplaneSearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAirplaneSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_airplane_search)
        binding.search = this
        binding.lifecycleOwner = this

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = ViewPagerAdapter(listOf(OneWaySearchFragment(), RoundTripSearchFragment()), this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.one_way)
                1 -> tab.text = getString(R.string.round_trip)
            }
        }.attach()

        lifecycleScope.launch {
            MobileAds.initialize(this@AirplaneSearchActivity)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }

    }
}