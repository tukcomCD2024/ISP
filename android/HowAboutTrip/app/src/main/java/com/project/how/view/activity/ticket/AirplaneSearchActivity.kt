package com.project.how.view.activity.ticket

import android.os.Bundle
import android.view.View
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
import com.project.how.interface_af.OnLoadListener
import com.project.how.view.fragment.ticket.OneWaySearchFragment
import com.project.how.view.fragment.ticket.RoundTripSearchFragment
import kotlinx.coroutines.launch

class AirplaneSearchActivity : AppCompatActivity(), OnLoadListener {
    private lateinit var binding : ActivityAirplaneSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_airplane_search)
        binding.search = this
        binding.lifecycleOwner = this

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = ViewPagerAdapter(listOf(OneWaySearchFragment(this), RoundTripSearchFragment(this)), this)

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

    private fun load(){
        binding.loadingBackground.visibility = View.VISIBLE
        binding.loadingInfo.visibility = View.VISIBLE
        binding.loadingLottie.visibility = View.VISIBLE
        binding.loadingLottie.playAnimation()
    }

    private fun stopLoaing(){
        binding.loadingBackground.visibility = View.GONE
        binding.loadingInfo.visibility = View.GONE
        binding.loadingLottie.visibility = View.GONE
        binding.loadingLottie.pauseAnimation()
    }

    override fun onLoadStartListener() {
        load()
    }

    override fun onLoadFinishListener() {
        stopLoaing()
    }
}