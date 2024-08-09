package com.project.how.view.activity.ticket

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.adapter.recyclerview.booking.airplane.RoundTripAirplaneListAdapter
import com.project.how.data_class.dto.booking.airplane.GenerateSkyscannerUrlRequest
import com.project.how.data_class.dto.booking.airplane.GetFlightOffersRequest
import com.project.how.data_class.dto.booking.airplane.GetFlightOffersResponseElement
import com.project.how.data_class.dto.booking.airplane.LikeFlightElement
import com.project.how.data_class.dto.booking.airplane.RoundTripFlightOffers
import com.project.how.data_class.recyclerview.ticket.FlightMember
import com.project.how.data_class.roomdb.RecentAirplane
import com.project.how.databinding.ActivityRoundTripAirplaneListBinding
import com.project.how.view.activity.calendar.CalendarEditActivity
import com.project.how.view_model.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoundTripAirplaneListActivity : AppCompatActivity(), RoundTripAirplaneListAdapter.OnItemClickListener {
    private lateinit var binding : ActivityRoundTripAirplaneListBinding
    private val bookingViewModel : BookingViewModel by viewModels()
    private lateinit var adapter : RoundTripAirplaneListAdapter
    private lateinit var data : ArrayList<GetFlightOffersResponseElement>
    private lateinit var input : GetFlightOffersRequest
    private lateinit var lid : MutableList<Long>
    private var clicked : GetFlightOffersResponseElement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_round_trip_airplane_list)
        binding.airport = this
        binding.lifecycleOwner = this

        data = CalendarEditActivity.getSerializable(this, getString(R.string.round_trip_flight_offers), RoundTripFlightOffers::class.java).data
        lid = MutableList<Long>(data.size) {-1L}
        bookingViewModel.getLikeFlightList(lid)
        Log.d("RoundTripAirplaneListActivity", "data.size = ${data.size}\ndata[0] : ${data[0].id}")

        adapter = RoundTripAirplaneListAdapter(this, data, lid, this, null)
        binding.airplaneList.adapter = adapter

        input = CalendarEditActivity.getSerializable(this,
            getString(R.string.get_flight_offers_request), GetFlightOffersRequest::class.java)

        bookingViewModel.skyscannerUrlLiveData.observe(this){url->
            if (clicked != null){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                val recent = RecentAirplane(
                    name = getString(R.string.recent_round_trip_name, input.departure, input.destination),
                    image = null,
                    des = input.departure,
                    time1 = getString(R.string.date_text, clicked!!.abroadDepartureTime, clicked!!.abroadArrivalTime),
                    time2 = getString(R.string.date_text, clicked!!.homeDepartureTime, clicked!!.homeArrivalTime),
                    skyscannerUrl = url
                )
                bookingViewModel.addRecentAirplane(recent)
            }
        }

        bookingViewModel.likeFlightListLiveData.observe(this){
            lid = it
            adapter.unlock()
            adapter.updateLike(lid)

        }

        lifecycleScope.launch {
            MobileAds.initialize(this@RoundTripAirplaneListActivity)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }
    }

    override fun onItemClickerListener(
        data: GetFlightOffersResponseElement,
        flightMember: FlightMember?
    ) {
        lifecycleScope.launch {
            clicked = data
            val request =
                GenerateSkyscannerUrlRequest(
                    data.departureIataCode,
                    data.arrivalIataCode,
                    input.departureDate,
                    input.returnDate,
                    input.adults,
                    input.children,
                    data.abroadDuration,
                    data.transferCount
                )
            bookingViewModel.generateSkyscannerUrl(request).collect{ check->
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

    override fun onHeartClickerListener(
        check: Boolean,
        data: GetFlightOffersResponseElement,
        position: Int,
        id: Long
    ) {
        lifecycleScope.launch{
            if (check){
                bookingViewModel.unlike(id, position).collect{ c->
                    when(c){
                        BookingViewModel.NOT_EXIST->{
                            Toast.makeText(this@RoundTripAirplaneListActivity,
                                getString(R.string.not_exist_flight), Toast.LENGTH_SHORT).show()
                        }
                        BookingViewModel.NOT_MINE->{
                            Toast.makeText(this@RoundTripAirplaneListActivity,
                                getString(R.string.not_mine_like), Toast.LENGTH_SHORT).show()
                        }
                        BookingViewModel.NETWORK_FAILED->{
                            Toast.makeText(this@RoundTripAirplaneListActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                val lowf = LikeFlightElement(
                    data.carrierCode,
                    data.totalPrice,
                    data.abroadDuration,
                    data.abroadDepartureTime,
                    data.abroadArrivalTime,
                    data.homeDuration,
                    data.homeDepartureTime,
                    data.homeArrivalTime,
                    data.departureIataCode,
                    data.arrivalIataCode,
                    data.nonstop,
                    data.transferCount,
                    input.adults,
                    input.children
                )
                bookingViewModel.like(lowf, position).collect{ c->
                    if (c != BookingViewModel.SUCCESS){
                        Toast.makeText(this@RoundTripAirplaneListActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}