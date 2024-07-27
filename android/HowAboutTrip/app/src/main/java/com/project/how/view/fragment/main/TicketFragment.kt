package com.project.how.view.fragment.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.project.how.R
import com.project.how.adapter.recyclerview.booking.airplane.RecentAirplaneAdapter
import com.project.how.adapter.recyclerview.booking.hotel.RecentHotelAdapter
import com.project.how.adapter.recyclerview.viewpager.EventTicketViewPagerAdapter
import com.project.how.data_class.recyclerview.EventViewPager
import com.project.how.data_class.recyclerview.RecentHotel
import com.project.how.data_class.roomdb.RecentAirplane
import com.project.how.databinding.FragmentTicketBinding
import com.project.how.view.activity.mypage.LikeActivity
import com.project.how.view.activity.ticket.AirplaneSearchActivity
import com.project.how.view.dialog.bottom_sheet_dialog.WebViewBottomSheetDialog
import com.project.how.view_model.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketFragment : Fragment(), RecentHotelAdapter.OnItemClickListener, RecentAirplaneAdapter.OnItemClickListener {
    private var _binding : FragmentTicketBinding? = null
    private val binding : FragmentTicketBinding
        get() = _binding!!
    private val bookingViewModel : BookingViewModel by viewModels()
    private lateinit var eventAdapter : EventTicketViewPagerAdapter
    private lateinit var recentAirplaneAdapter: RecentAirplaneAdapter
    private lateinit var recentHotelAdapter: RecentHotelAdapter
    private lateinit var recentAirplane : List<RecentAirplane>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var eventTest = mutableListOf<EventViewPager>()
        var recentHotelTest = mutableListOf<RecentHotel>()

        eventTest.add(EventViewPager("test", "2024 파리 올림픽\n여름방학", R.drawable.event_image_paris_temp, true))
        eventTest.add(EventViewPager("test", "이번 여름방학엔\n일본여행가기", R.drawable.event_image_japan_temp, true))
        eventTest.add(EventViewPager("test", "니스 최고의 해변에서\n휴양하기", R.drawable.event_image_nis_temp, true))
        eventTest.add(EventViewPager("test", "2024 파리 올림픽\n여름방학", R.drawable.event_image_paris_temp, true))
        eventTest.add(EventViewPager("test", "항공권 검색으로\n편안한 여행", R.drawable.event_image_airplnae_temp, true))

        for (i in 0..5) {
            recentHotelTest.add(
                RecentHotel(
                    i.toLong(),
                    null,
                    "호텔 테스트 $i",
                    "3/${25 + i}",
                    "2 Chome-2-1 Yoyogi, Shibuya City, Tokyo 151-0053 일본"
                )
            )
        }
        eventAdapter = EventTicketViewPagerAdapter(eventTest)
        recentHotelAdapter = RecentHotelAdapter(recentHotelTest.toList(), this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ticket, container, false)
        binding.ticket = this
        binding.lifecycleOwner = viewLifecycleOwner
        bookingViewModel.recentAirplaneLiveData.observe(viewLifecycleOwner){recent->
            if(recent.isEmpty()){
                recentAirplane = listOf(
                    RecentAirplane(
                        name = "항공권 검색 기능을 이용해보세요.",
                        image = null,
                        des = "Empty",
                        time1 = "",
                        time2 = null,
                        skyscannerUrl = null
                    )
                )
            }else{
                recentAirplane = recent
            }
            recentAirplaneAdapter = RecentAirplaneAdapter(recentAirplane.toList(), this)
            binding.recentAirplane.adapter = recentAirplaneAdapter
        }

        binding.event.adapter = eventAdapter
        binding.recentHotel.adapter = recentHotelAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.indicator, binding.event) { _, _ -> }.attach()
    }

    override fun onStart() {
        super.onStart()
        bookingViewModel.fetchRecentAirplanes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickListener(id: Long) {
    }

    fun moveAirplaneSearch(){
        startActivity(Intent(activity, AirplaneSearchActivity::class.java))
    }

    fun moveLike(){
        startActivity(Intent(requireContext(), LikeActivity::class.java))
    }

    override fun onItemClickListener(url: String) {
        val web = WebViewBottomSheetDialog(url)
        web.show(childFragmentManager, "WebViewBottomSheetDialog")
    }
}