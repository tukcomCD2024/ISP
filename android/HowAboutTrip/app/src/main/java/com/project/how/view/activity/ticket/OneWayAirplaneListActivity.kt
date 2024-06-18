package com.project.how.view.activity.ticket

import android.os.Bundle
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
import com.project.how.data_class.dto.LikeOneWayFlightElement
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
    private lateinit var lid : MutableList<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_one_way_airplane_list)
        binding.airport = this
        binding.lifecycleOwner = this

        data = CalendarEditActivity.getSerializable(this,
            getString(R.string.one_way_flight_offers), OneWayFlightOffers::class.java).data
        lid = MutableList<Long>(data.size) { -1L }
        bookingViewModel.getLikeFlightList(lid)
        adapter = OneWayAirplaneListAdapter(this, data, lid,this)

        input = CalendarEditActivity.getSerializable(this, getString(R.string.get_one_way_flight_offers_request), GetOneWayFlightOffersRequest::class.java)

        binding.airplaneList.adapter = adapter

        bookingViewModel.skyscannerUrlLiveData.observe(this){url->
            val web = WebViewBottomSheetDialog(url)
            web.show(supportFragmentManager, "WebViewBottomSheetDialog")
        }

        bookingViewModel.likeFlightListLiveData.observe(this){
            lid = it
            adapter.unlock()
            adapter.updateLike(lid)

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
                input.adults,
                input.children,
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

    override fun onHeartClickerListener(
        check: Boolean,
        data: GetOneWayFlightOffersResponseElement,
        position: Int,
        id: Long
    ){
        lifecycleScope.launch{
            if (check){
                bookingViewModel.unlike(this@OneWayAirplaneListActivity, MemberViewModel.tokensLiveData.value!!.accessToken, id, position).collect{c->
                    when(c){
                        BookingViewModel.NOT_EXIST->{
                            Toast.makeText(this@OneWayAirplaneListActivity,
                                getString(R.string.not_exist_flight), Toast.LENGTH_SHORT).show()
                        }
                        BookingViewModel.NOT_MINE->{
                            Toast.makeText(this@OneWayAirplaneListActivity,
                                getString(R.string.not_mine_like), Toast.LENGTH_SHORT).show()
                        }
                        BookingViewModel.NETWORK_FAILED->{
                            Toast.makeText(this@OneWayAirplaneListActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                val lowf = LikeOneWayFlightElement(
                    data.carrierCode,
                    data.totalPrice,
                    data.departureIataCode,
                    data.arrivalIataCode,
                    data.abroadDuration,
                    data.abroadDepartureTime,
                    data.abroadArrivalTime,
                    data.nonstop,
                    data.transferCount
                )
                bookingViewModel.like(this@OneWayAirplaneListActivity, MemberViewModel.tokensLiveData.value!!.accessToken, lowf, position).collect{c->
                    if (c != BookingViewModel.SUCCESS){
                        Toast.makeText(this@OneWayAirplaneListActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}