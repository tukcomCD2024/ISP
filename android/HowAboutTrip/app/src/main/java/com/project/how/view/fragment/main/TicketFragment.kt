package com.project.how.view.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.project.how.R
import com.project.how.adapter.recyclerview.RecentAirplaneAdapter
import com.project.how.adapter.recyclerview.RecentHotelAdapter
import com.project.how.adapter.recyclerview.viewpager.EventTicketViewPagerAdapter
import com.project.how.data_class.recyclerview.EventViewPager
import com.project.how.data_class.recyclerview.RecentAirplane
import com.project.how.data_class.recyclerview.RecentHotel
import com.project.how.databinding.FragmentTicketBinding

class TicketFragment : Fragment(), RecentHotelAdapter.OnItemClickListener {
    private var _binding : FragmentTicketBinding? = null
    private val binding : FragmentTicketBinding
        get() = _binding!!
    private lateinit var eventAdapter : EventTicketViewPagerAdapter
    private lateinit var recentAirplaneAdapter: RecentAirplaneAdapter
    private lateinit var recentHotelAdapter: RecentHotelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var eventTest = mutableListOf<EventViewPager>()
        var recentAirplaneTest = mutableListOf<RecentAirplane>()
        var recentHotelTest = mutableListOf<RecentHotel>()

        for (i in 0..5){
            eventTest.add(EventViewPager("test", "항공 티켓을\n구매하세요.$i", null))
            recentAirplaneTest.add(RecentAirplane(i.toLong(), null, "Test$i", "(3/${25 + i})", "AM 07:15 ~  AM 09:30"))
            recentHotelTest.add(RecentHotel(i.toLong(), null, "호텔 테스트 $i", "3/${25 + i}", "2 Chome-2-1 Yoyogi, Shibuya City, Tokyo 151-0053 일본"))
        }

        eventAdapter = EventTicketViewPagerAdapter(eventTest)
        recentAirplaneAdapter = RecentAirplaneAdapter(recentAirplaneTest.toList())
        recentHotelAdapter = RecentHotelAdapter(recentHotelTest.toList(), this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ticket, container, false)
        binding.ticket = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.event.adapter = eventAdapter
        binding.recentAirplane.adapter = recentAirplaneAdapter
        binding.recentHotel.adapter = recentHotelAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.indicator, binding.event) { _, _ -> }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickListener(id: Long) {
    }
}