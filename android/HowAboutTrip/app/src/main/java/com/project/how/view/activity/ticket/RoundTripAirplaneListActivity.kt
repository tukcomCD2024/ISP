package com.project.how.view.activity.ticket

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.adapter.recyclerview.RoundTripAirplaneListAdapter
import com.project.how.data_class.dto.GenerateOneWaySkyscannerUrlRequest
import com.project.how.data_class.dto.GenerateSkyscannerUrlRequest
import com.project.how.data_class.dto.GetFlightOffersRequest
import com.project.how.data_class.dto.GetFlightOffersResponseElement
import com.project.how.data_class.dto.GetOneWayFlightOffersRequest
import com.project.how.data_class.dto.RoundTripFlightOffers
import com.project.how.databinding.ActivityRoundTripAirplaneListBinding
import com.project.how.view.activity.calendar.CalendarEditActivity
import com.project.how.view.dialog.bottom_sheet_dialog.WebViewBottomSheetDialog
import com.project.how.view_model.BookingViewModel
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.launch

class RoundTripAirplaneListActivity : AppCompatActivity(), RoundTripAirplaneListAdapter.OnItemClickListener {
    private lateinit var binding : ActivityRoundTripAirplaneListBinding
    private val bookingViewModel : BookingViewModel by viewModels()
    private lateinit var adapter : RoundTripAirplaneListAdapter
    private lateinit var data : ArrayList<GetFlightOffersResponseElement>
    private lateinit var input : GetFlightOffersRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_round_trip_airplane_list)
        binding.airport = this
        binding.lifecycleOwner = this

        data = CalendarEditActivity.getSerializable(this, getString(R.string.round_trip_flight_offers), RoundTripFlightOffers::class.java).data
        Log.d("RoundTripAirplaneListActivity", "data.size = ${data.size}\ndata[0] : ${data[0].id}")

        adapter = RoundTripAirplaneListAdapter(this, data, this)
        binding.airplaneList.adapter = adapter

        input = CalendarEditActivity.getSerializable(this,
            getString(R.string.get_flight_offers_request), GetFlightOffersRequest::class.java)

        bookingViewModel.skyscannerUrlLiveData.observe(this){url->
            val web = WebViewBottomSheetDialog(url)
            web.show(supportFragmentManager, "WebViewBottomSheetDialog")
        }

        lifecycleScope.launch {
            MobileAds.initialize(this@RoundTripAirplaneListActivity)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }
    }

    override fun onItemClickerListener(data: GetFlightOffersResponseElement) {
        lifecycleScope.launch {
            val request = GenerateSkyscannerUrlRequest(
                data.departureIataCode,
                data.arrivalIataCode,
                input.departureDate,
                input.returnDate,
                input.adults,
                input.children,
                data.abroadDuration,
                data.transferCount
            )
            bookingViewModel.generateSkyscannerUrl(this@RoundTripAirplaneListActivity, MemberViewModel.tokensLiveData.value!!.accessToken, request).collect{ check->
                when(check){
                    BookingViewModel.NOT_EXIST->{
                        Toast.makeText(this@RoundTripAirplaneListActivity, getString(R.string.not_exist_flight_offers), Toast.LENGTH_SHORT).show()
                    }
                    BookingViewModel.NETWORK_FAILED->{
                        Toast.makeText(this@RoundTripAirplaneListActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onHeartClickerListener() {

    }
}