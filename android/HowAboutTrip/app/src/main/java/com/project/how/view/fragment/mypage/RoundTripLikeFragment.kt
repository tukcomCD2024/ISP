package com.project.how.view.fragment.mypage

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
import com.project.how.adapter.recyclerview.OneWayAirplaneListAdapter
import com.project.how.adapter.recyclerview.RoundTripAirplaneListAdapter
import com.project.how.data_class.dto.GenerateSkyscannerUrlRequest
import com.project.how.data_class.dto.GetFlightOffersResponseElement
import com.project.how.data_class.dto.GetOneWayFlightOffersResponseElement
import com.project.how.data_class.dto.LikeFlightElement
import com.project.how.databinding.FragmentRoundTripLikeBinding
import com.project.how.view_model.BookingViewModel
import com.project.how.view_model.MemberViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoundTripLikeFragment : Fragment(), RoundTripAirplaneListAdapter.OnItemClickListener {
    private var _binding : FragmentRoundTripLikeBinding? = null
    private val binding : FragmentRoundTripLikeBinding
        get() = _binding!!
    private val bookingViewModel : BookingViewModel by viewModels()
    private val data = arrayListOf<GetFlightOffersResponseElement>()
    private lateinit var adapter: RoundTripAirplaneListAdapter
    private lateinit var lid : MutableList<Long>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_round_trip_like, container, false)
        lifecycleScope.launch {
            bookingViewModel.getLikeFlight(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken).collect{ check->
                if (check != BookingViewModel.SUCCESS){
                    Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        bookingViewModel.likeFlightLiveData.observe(viewLifecycleOwner){likes->
            lid = mutableListOf<Long>()
            data.clear()

            likes.forEachIndexed { index, d ->
                if (d.homeDuration != null){
                    lid.add(d.id)
                    data.add(
                        GetFlightOffersResponseElement(
                            d.id.toString(),
                            d.carrierCode,
                            d.totalPrice,
                            d.abroadDuration,
                            d.abroadDepartureTime,
                            d.abroadArrivalTime,
                            d.homeDuration,
                            d.homeDepartureTime!!,
                            d.homeArrivalTime!!,
                            d.departureIataCode,
                            d.arrivalIataCode,
                            if (d.transferCount > 0.toString()) false else true,
                            d.transferCount.toLong()
                        )
                    )
                }
            }
            adapter = RoundTripAirplaneListAdapter(requireContext(), data, lid,this)
            binding.airplaneList.adapter = adapter
            adapter.unlock()
        }
        bookingViewModel.likeFlightListLiveData.observe(viewLifecycleOwner){
            lid = it
        }
        return binding.root

    }

    override fun onItemClickerListener(data: GetFlightOffersResponseElement) {

    }

    override fun onHeartClickerListener(
        check: Boolean,
        data: GetFlightOffersResponseElement,
        position: Int,
        id: Long
    ) {
        lifecycleScope.launch {
            if (check){
                bookingViewModel.unlike(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken, id, position).collect{c->
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
            }else{
                val lf = LikeFlightElement(
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
                    data.transferCount
                )
                bookingViewModel.like(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken, lf, position).collect{c->
                    if (c != BookingViewModel.SUCCESS){
                        Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}