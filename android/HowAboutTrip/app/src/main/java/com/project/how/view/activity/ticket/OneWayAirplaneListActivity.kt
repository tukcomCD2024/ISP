package com.project.how.view.activity.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.adapter.recyclerview.OneWayAirplaneListAdapter
import com.project.how.data_class.dto.GenerateOneWaySkyscannerUrlRequest
import com.project.how.data_class.dto.GetOneWayFlightOffersRequest
import com.project.how.data_class.dto.GetOneWayFlightOffersResponseElement
import com.project.how.data_class.dto.OneWayFlightOffers
import com.project.how.databinding.ActivityOneWayAirplaneListBinding
import com.project.how.view.activity.calendar.CalendarEditActivity
import com.project.how.view.dialog.bottom_sheet_dialog.WebViewBottomSheetDialog
import com.project.how.view_model.BookingViewModel
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.launch

class OneWayAirplaneListActivity : AppCompatActivity(), OneWayAirplaneListAdapter.OnItemClickListener {
    private lateinit var binding : ActivityOneWayAirplaneListBinding
    private val bookingViewModel : BookingViewModel by viewModels()
    private lateinit var adapter : OneWayAirplaneListAdapter
    private lateinit var data : ArrayList<GetOneWayFlightOffersResponseElement>
    private lateinit var input : GetOneWayFlightOffersRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_one_way_airplane_list)
        binding.airport = this
        binding.lifecycleOwner = this

        data = CalendarEditActivity.getSerializable(this,
            getString(R.string.one_way_flight_offers), OneWayFlightOffers::class.java).data
        adapter = OneWayAirplaneListAdapter(this, data, this)

        input = CalendarEditActivity.getSerializable(this, getString(R.string.get_one_way_flight_offers_request), GetOneWayFlightOffersRequest::class.java)

        binding.airplaneList.adapter = adapter

        bookingViewModel.skyscannerUrlLiveData.observe(this){url->
            val web = WebViewBottomSheetDialog(url)
            web.show(supportFragmentManager, "WebViewBottomSheetDialog")
        }

        lifecycleScope.launch {
            MobileAds.initialize(this@OneWayAirplaneListActivity)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }

    }

    override fun onItemClickerListener(data: GetOneWayFlightOffersResponseElement) {
        lifecycleScope.launch {
            val request = GenerateOneWaySkyscannerUrlRequest(
                data.departureIataCode,
                data.arrivalIataCode,
                input.departureDate,
                input.adults + input.children,
                0,
                data.abroadDuration,
                data.transferCount
            )
            bookingViewModel.generateOneWaySkyscannerUrl(this@OneWayAirplaneListActivity, MemberViewModel.tokensLiveData.value!!.accessToken, request).collect{check->
                when(check){
                    BookingViewModel.NOT_EXIST->{
                        Toast.makeText(this@OneWayAirplaneListActivity, getString(R.string.not_exist_flight_offers), Toast.LENGTH_SHORT).show()
                    }
                    BookingViewModel.NETWORK_FAILED->{
                        Toast.makeText(this@OneWayAirplaneListActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onHeartClickerListener() {

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack()
        } else {
            binding.webview.visibility = View.GONE
            binding.webview.loadUrl("about:blank")
            binding.webview.clearCache(true)
            binding.webview.clearHistory()
        }
    }
}