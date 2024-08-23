package com.project.how.view.fragment.mypage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.adapter.recyclerview.booking.airplane.OneWayAirplaneListAdapter
import com.project.how.data_class.dto.booking.airplane.GenerateOneWaySkyscannerUrlRequest
import com.project.how.data_class.dto.booking.airplane.GenerateSkyscannerUrlRequest
import com.project.how.data_class.dto.booking.airplane.GetOneWayFlightOffersResponseElement
import com.project.how.data_class.recyclerview.ticket.FlightMember
import com.project.how.data_class.roomdb.RecentAirplane
import com.project.how.databinding.FragmentOneWayLikeBinding
import com.project.how.view_model.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OneWayLikeFragment : Fragment(), OneWayAirplaneListAdapter.OnItemClickListener{
    private var _binding : FragmentOneWayLikeBinding? = null
    private val binding : FragmentOneWayLikeBinding
        get() = _binding!!
    private val bookingViewModel : BookingViewModel by viewModels()
    private val data = arrayListOf<GetOneWayFlightOffersResponseElement>()
    private lateinit var adapter : OneWayAirplaneListAdapter
    private lateinit var lid : MutableList<Long>
    private lateinit var member : MutableList<FlightMember>
    private var clicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_one_way_like, container, false)
        binding.like = this
        binding.lifecycleOwner = viewLifecycleOwner
        bookingViewModel.likeFlightLiveData.observe(viewLifecycleOwner){likes->
            lid = mutableListOf<Long>()
            member = mutableListOf()
            data.clear()
            
            likes.forEachIndexed { index, d ->
                if (d.homeDuration == null){
                    lid.add(d.id)
                    member.add(FlightMember(d.adult, d.children))
                    data.add(
                        GetOneWayFlightOffersResponseElement(
                            d.id.toString(),
                            d.carrierCode,
                            d.totalPrice,
                            d.departureIataCode,
                            d.arrivalIataCode,
                            d.transferCount <= 0.toString(),
                            d.transferCount.toLong(),
                            d.abroadDuration,
                            d.abroadDepartureTime,
                            d.abroadArrivalTime
                        )
                    )
                }
            }
            adapter = OneWayAirplaneListAdapter(requireContext(), data, lid, this, member)
            binding.airplaneList.adapter = adapter
            adapter.unlock()
        }
        bookingViewModel.likeFlightListLiveData.observe(viewLifecycleOwner){
            lid = it
        }
        bookingViewModel.skyscannerUrlLiveData.observe(viewLifecycleOwner){url->
            if (clicked){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                clicked = false
            }
        }
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            bookingViewModel.getLikeFlight().collect{ check->
                if (check != BookingViewModel.SUCCESS){
                    Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickerListener(
        data: GetOneWayFlightOffersResponseElement,
        flightMember: FlightMember?
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            clicked = true
            bookingViewModel.generateOneWaySkyscannerUrl(
                GenerateOneWaySkyscannerUrlRequest(
                    data.departureIataCode,
                    data.arrivalIataCode,
                    data.abroadDepartureTime.split(" ")[0],
                    flightMember?.adult ?: 1L,
                    flightMember?.children ?: 0L,
                    data.abroadDuration,
                    data.transferCount
                )
            ).collect{
                if (it != BookingViewModel.SUCCESS)
                    clicked = false
            }
        }
    }

    override fun onHeartClickerListener(
        check: Boolean,
        data: GetOneWayFlightOffersResponseElement,
        position: Int,
        id: Long
    ) {
        lifecycleScope.launch{
            if (check){
                bookingViewModel.unlike(id, position).collect{ c->
                    when(c){
                        BookingViewModel.SUCCESS->{
                            adapter.remove(position)
                        }
                        BookingViewModel.NOT_EXIST->{
                            Toast.makeText(requireContext(),
                                getString(R.string.not_exist_flight), Toast.LENGTH_SHORT).show()
                        }
                        BookingViewModel.NOT_MINE->{
                            Toast.makeText(requireContext(),
                                getString(R.string.not_mine_like), Toast.LENGTH_SHORT).show()
                        }
                        BookingViewModel.NETWORK_FAILED->{
                            Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }

}