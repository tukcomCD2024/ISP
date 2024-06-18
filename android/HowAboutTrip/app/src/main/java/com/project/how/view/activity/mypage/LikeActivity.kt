package com.project.how.view.activity.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.project.how.R
import com.project.how.adapter.viewpager.ViewPagerAdapter
import com.project.how.databinding.ActivityLikeBinding
import com.project.how.view.fragment.mypage.OneWayLikeFragment
import com.project.how.view.fragment.mypage.HotelLikeFragment
import com.project.how.view.fragment.mypage.RoundTripLikeFragment

class LikeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLikeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_like)
        binding.like = this
        binding.lifecycleOwner = this

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = ViewPagerAdapter(listOf(OneWayLikeFragment(), RoundTripLikeFragment(), HotelLikeFragment()), this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.one_way)
                1 -> tab.text = getString(R.string.round_trip)
                2 -> tab.text = getString(R.string.hotel)
            }
        }.attach()
    }
}