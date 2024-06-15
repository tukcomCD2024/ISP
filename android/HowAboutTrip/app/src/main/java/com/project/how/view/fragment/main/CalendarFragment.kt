package com.project.how.view.fragment.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayoutMediator
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.viewpager.EventViewPagerAdapter
import com.project.how.adapter.recyclerview.RecentAddedCalendarsAdapter
import com.project.how.data_class.recyclerview.EventViewPager
import com.project.how.data_class.recyclerview.RecentAddedCalendar
import com.project.how.data_class.recyclerview.Schedule
import com.project.how.data_class.dto.GetCountryLocationResponse
import com.project.how.databinding.FragmentCalendarBinding
import com.project.how.interface_af.OnDesListener
import com.project.how.view.activity.ai.AddAICalendarActivity
import com.project.how.view.activity.calendar.CalendarEditActivity
import com.project.how.view.activity.calendar.CalendarListActivity
import com.project.how.view.dialog.bottom_sheet_dialog.DesBottomSheetDialog
import com.project.how.view_model.MemberViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

class CalendarFragment : Fragment(), OnDesListener {
    private var _binding : FragmentCalendarBinding? = null
    private val binding : FragmentCalendarBinding
        get() = _binding!!
    private val scheduleViewModel : ScheduleViewModel by viewModels()
    private lateinit var nowDate : LocalDate
    private lateinit var nowDateString : String
    private var nearScheduleDate: Long = -1
    private val event = mutableListOf<EventViewPager>()
    private val recentAddedCalendar = mutableListOf<RecentAddedCalendar>()
    private lateinit var eventAdapter : EventViewPagerAdapter
    private lateinit var recentAddedCalendarAdapter: RecentAddedCalendarsAdapter
    private var destination : String? = null
    private var departureDate : String? = null
    private var entranceDate : String? = null
    private var latLng : GetCountryLocationResponse? = null
    private var dday : Long = -1
    private lateinit var autoScrollJob : Job
    private var viewPagerPosition = 0
    var dDay : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nowDate = LocalDate.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        nowDateString = nowDate.format(dateFormat)
        for(i in 0..5){
            event.add(EventViewPager("test", "일정 생성을\n해보세요$i", null))
            recentAddedCalendar.add(RecentAddedCalendar(-1, "test용 목적지$i", mutableListOf<String>("test1", "test2", "test3", "test4", "test5", "test6", "tteeesss7", "test8"), "https://img.freepik.com/free-photo/vertical-shot-beautiful-eiffel-tower-captured-paris-france_181624-45445.jpg?w=740&t=st=1708260600~exp=1708261200~hmac=01d8abec61f555d0edb040d41ce8ea39904853aea6df7c37ce0b5a35e07c1954"))
        }
        recentAddedCalendarAdapter = RecentAddedCalendarsAdapter(recentAddedCalendar)
        eventAdapter = EventViewPagerAdapter(event)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.calendar = this
        binding.lifecycleOwner = viewLifecycleOwner
        autoScrollJobCreate()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager2.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewPagerPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state){
                    ViewPager2.SCROLL_STATE_IDLE -> {if (!autoScrollJob.isActive) autoScrollJobCreate()}
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        autoScrollJob.cancel()
                    }
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()

        scheduleViewModel.getScheduleList(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken)
        scheduleViewModel.scheduleListLiveData.observe(viewLifecycleOwner){list->
            if(list.isNotEmpty()){
                var near = -1
                var nearDateString = "2050-12-31"
                list.forEachIndexed { index, getScheduleListResponseElement ->
                    if (nearDateString > getScheduleListResponseElement.startDate && getScheduleListResponseElement.startDate >= nowDateString){
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

        binding.recentAddedCalendar.adapter = recentAddedCalendarAdapter
        binding.viewPager2.adapter = eventAdapter
        TabLayoutMediator(binding.indicator, binding.viewPager2) { _, _ -> }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun autoScrollJobCreate() {
        autoScrollJob = lifecycleScope.launch{

        }
    }

    fun add() {
        showDesInput()
    }

    private fun showDesInput(){
        val des = DesBottomSheetDialog(this)
        des.show(childFragmentManager, "DesBottomSheetDialog")
    }

    fun moveAddAICalendar(){
        startActivity(Intent(activity, AddAICalendarActivity::class.java))
    }

    fun moveCalendarList(){
        startActivity(Intent(activity, CalendarListActivity::class.java))
    }

    private fun showCalendar(){
        val constraints = CalendarConstraints.Builder()
            .setStart(Calendar.getInstance().timeInMillis)
            .build()

        val calendar = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setCalendarConstraints(constraints)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
        calendar.show(childFragmentManager, "MaterialDatePicker")

        calendar.addOnPositiveButtonClickListener {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it.first
            val format = SimpleDateFormat("yyyy-MM-dd")
            val formatted = format.format(utc.time)
            utc.timeInMillis = it.second
            val formattedSecond = format.format(utc.time)
            entranceDate = formattedSecond
            departureDate = formatted
            val intent = Intent(activity, CalendarEditActivity::class.java)
            val schedule = Schedule(
                destination!!,
                destination!!,
                departureDate!!,
                entranceDate!!,
                0,
                scheduleViewModel.getEmptyDaysSchedule(departureDate!!, entranceDate!!)
            )
            intent.putExtra(getString(R.string.type), CalendarEditActivity.NEW)
            intent.putExtra(getString(R.string.schedule), schedule)
            intent.putExtra(getString(R.string.server_calendar_latitude), latLng!!.lat)
            intent.putExtra(getString(R.string.server_calendar_longitude), latLng!!.lng)
            startActivity(intent)
        }
    }

    override fun onDesListener(des: String) {
        lifecycleScope.launch {
            scheduleViewModel.getCountryLocation(des).collect{ location ->
                location?.let {
                    destination = des
                    latLng = location
                    showCalendar()
                } ?: run {
                    scheduleViewModel.getCountryLocation(des).collect { newLocation ->
                        newLocation?.let {
                            destination = des
                            latLng = newLocation
                            showCalendar()
                        } ?: run {
                            Toast.makeText(activity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}