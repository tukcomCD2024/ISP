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
import com.project.how.data_class.dto.GetOneWayFlightOffersResponse
import com.project.how.data_class.dto.GetOneWayFlightOffersResponseElement
import com.project.how.data_class.dto.LikeOneWayFlightElement
import com.project.how.databinding.FragmentAirplaneLikeBinding
import com.project.how.view_model.BookingViewModel
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.launch

class AirPlaneLikeFragment : Fragment(), OneWayAirplaneListAdapter.OnItemClickListener {
    private var _binding : FragmentAirplaneLikeBinding? = null
    private val binding : FragmentAirplaneLikeBinding
        get() = _binding!!
    private val bookingViewModel : BookingViewModel by viewModels()
    private val data = arrayListOf<GetOneWayFlightOffersResponseElement>()
    private lateinit var adapter : OneWayAirplaneListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_airplane_like, container, false)
        binding.like = this
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            bookingViewModel.getLikeFlight(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken).collect{check->
                if (check != BookingViewModel.SUCCESS){
                    Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        bookingViewModel.likeFlightLiveData.observe(viewLifecycleOwner){likes->
            likes.forEachIndexed { index, d ->
                data.add(GetOneWayFlightOffersResponseElement(
                    d.id.toString(),
                    d.carrierCode,
                    d.totalPrice,
                    d.departureIataCode,
                    d.arrivalIataCode,
                    if (d.transferCount> 0.toString()) false else true,
                    d.transferCount.toLong(),
                    d.abroadDuration,
                    d.abroadDepartureTime,
                    d.abroadArrivalTime
                ))
            }
            adapter = OneWayAirplaneListAdapter(requireContext(), data, this)
            binding.airplaneList.adapter = adapter
        }
        return binding.root
    }

    override fun onItemClickerListener(data: GetOneWayFlightOffersResponseElement) {

    }

    override fun onHeartClickerListener(
        check: Boolean,
        data: GetOneWayFlightOffersResponseElement
    ) {
        lifecycleScope.launch{
            if (check){
                this@AirPlaneLikeFragment.data.remove(data)
                adapter.notifyDataSetChanged()
                bookingViewModel
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
                bookingViewModel.like(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken, lowf).collect{c->
                    if (c == BookingViewModel.SUCCESS){
                    }else{
                        Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

}