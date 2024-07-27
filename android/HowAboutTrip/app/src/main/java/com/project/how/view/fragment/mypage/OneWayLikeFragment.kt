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
import com.project.how.adapter.recyclerview.booking.airplane.OneWayAirplaneListAdapter
import com.project.how.data_class.dto.booking.airplane.GetOneWayFlightOffersResponseElement
import com.project.how.data_class.dto.booking.airplane.LikeOneWayFlightElement
import com.project.how.databinding.FragmentOneWayLikeBinding
import com.project.how.view_model.BookingViewModel
import com.project.how.view_model.MemberViewModel
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_one_way_like, container, false)
        binding.like = this
        binding.lifecycleOwner = viewLifecycleOwner
        bookingViewModel.likeFlightLiveData.observe(viewLifecycleOwner){likes->
            lid = mutableListOf<Long>()
            data.clear()
            
            likes.forEachIndexed { index, d ->
                if (d.homeDuration == null){
                    lid.add(d.id)
                    data.add(
                        GetOneWayFlightOffersResponseElement(
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
                    )
                    )
                }
            }
            adapter = OneWayAirplaneListAdapter(requireContext(), data, lid,this)
            binding.airplaneList.adapter = adapter
            adapter.unlock()
        }
        bookingViewModel.likeFlightListLiveData.observe(viewLifecycleOwner){
            lid = it
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        binding.airplaneList.adapter = null
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            bookingViewModel.getLikeFlight(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken).collect{ check->
                if (check != BookingViewModel.SUCCESS){
                    Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onItemClickerListener(data: GetOneWayFlightOffersResponseElement) {

    }

    override fun onHeartClickerListener(
        check: Boolean,
        data: GetOneWayFlightOffersResponseElement,
        position: Int,
        id: Long
    ) {
        lifecycleScope.launch{
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
                bookingViewModel.like(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken, lowf, position).collect{c->
                    if (c != BookingViewModel.SUCCESS){
                        Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

}