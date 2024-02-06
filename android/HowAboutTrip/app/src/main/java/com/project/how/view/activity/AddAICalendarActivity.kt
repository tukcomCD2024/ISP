package com.project.how.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.databinding.ActivityAddAicalendarBinding
import com.project.how.view.bottom_sheet_dialog.CalendarBottomSheetDialog

class AddAICalendarActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddAicalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_aicalendar)
        binding.ai = this
        binding.lifecycleOwner = this

        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    fun showCalendar(){
        val calendar = CalendarBottomSheetDialog()
        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
    }
}