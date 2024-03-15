package com.project.how.view.fragment.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.EventViewPagerAdapter
import com.project.how.data_class.EventViewPager
import com.project.how.databinding.FragmentCalendarBinding
import com.project.how.view.activity.ai.AddAICalendarActivity
import com.project.how.view.activity.calendar.CalendarListActivity
import com.project.how.view_model.MemberViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {
    private var _binding : FragmentCalendarBinding? = null
    private val binding : FragmentCalendarBinding
        get() = _binding!!
    private val scheduleViewModel : ScheduleViewModel by viewModels()
    private lateinit var nowDate : LocalDate
    private lateinit var nowDateString : String
    private var nearScheduleDate: Long = -1
    private val event = mutableListOf<EventViewPager>()
    private lateinit var adapter : EventViewPagerAdapter
    private var dday : Long = -1
    var dDay : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nowDate = LocalDate.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        nowDateString = nowDate.format(dateFormat)
        for(i in 0..5){
            event.add(EventViewPager("일정 생성을\n해보세요$i", null))
        }
        adapter = EventViewPagerAdapter(event)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.calendar = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleViewModel.getScheduleList(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken)
        scheduleViewModel.scheduleListLiveData.observe(viewLifecycleOwner){list->
            if(list.isNotEmpty()){
                var near = -1
                var nearDateString = "2050-12-31"
                list.forEachIndexed { index, getScheduleListResponseElement ->
                    if (nearDateString > getScheduleListResponseElement.startDate && getScheduleListResponseElement.startDate > nowDateString){
                        near = index
                        nearDateString = getScheduleListResponseElement.startDate
                    }
                }

                if (near == -1){
                    Glide.with(binding.root)
                        .load(R.drawable.event_viewpager_test)
                        .error(BuildConfig.ERROR_IMAGE_URl)
                        .into(binding.scheduleImage)
                }else{
                    val nearDate = LocalDate.parse(nearDateString, DateTimeFormatter.ISO_DATE)
                    val dateFormat = SimpleDateFormat("yyyyMMdd")
                    nearScheduleDate = dateFormat.parse(nearDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))?.time ?: -1
                    scheduleViewModel.getNearScheduleDay(nearScheduleDate)

                    binding.scheduleTitle.text = list[near].scheduleName
                    Glide.with(binding.root)
                        .load(list[near].imageUrl)
                        .error(BuildConfig.ERROR_IMAGE_URl)
                        .into(binding.scheduleImage)
                }

            }else{
                Glide.with(binding.root)
                    .load(R.drawable.event_viewpager_test)
                    .error(BuildConfig.ERROR_IMAGE_URl)
                    .into(binding.scheduleImage)
            }
        }

        scheduleViewModel.nearScheduleDayLiveData.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                scheduleViewModel.getDday().collect{
                    dday = it
                    dDay = resources.getString(R.string.d_day) + if (dday.toInt() == 0) getString(R.string.d_day_zero) else dday.toString()

                    Log.d("nearScheduleDayLiveData observe", "dday : $dday\ndDay : $dDay")

                    if(dday.toInt() == -1){
                        Log.d("nearScheduleDayLiveData observe", "dDay is INVISIBLE")
                        binding.dDay.visibility = View.INVISIBLE
                    }else{
                        Log.d("nearScheduleDayLiveData observe", "dDay is VISIBLE")
                        binding.dDay.visibility = View.VISIBLE
                        binding.dDay.text = dDay
                    }
                }
            }
        }

        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.indicator, binding.viewPager2) { _, _ -> }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun moveAddAICalendar(){
        startActivity(Intent(activity, AddAICalendarActivity::class.java))
    }

    fun moveCalendarList(){
        startActivity(Intent(activity, CalendarListActivity::class.java))
    }
}