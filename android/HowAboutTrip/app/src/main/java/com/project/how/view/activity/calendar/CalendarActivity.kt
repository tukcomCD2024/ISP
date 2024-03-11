package com.project.how.view.activity.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.tabs.TabLayout
import com.project.how.R
import com.project.how.adapter.recyclerview.DaysScheduleAdapter
import com.project.how.data_class.DaysSchedule
import com.project.how.data_class.Schedule
import com.project.how.data_class.ScheduleIDAndLatLng
import com.project.how.databinding.ActivityCalendarBinding
import com.project.how.view.dialog.AiScheduleDialog
import com.project.how.view.dialog.bottom_sheet_dialog.EditScheduleBottomSheetDialog
import com.project.how.view.dp.DpPxChanger
import com.project.how.view_model.MemberViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarActivity : AppCompatActivity(), DaysScheduleAdapter.OnDaysButtonClickListener, OnMapReadyCallback {
    private lateinit var binding : ActivityCalendarBinding
    private val viewModel : ScheduleViewModel by viewModels()
    private lateinit var supportMapFragment: SupportMapFragment
    private var selectedDays = 0
    private lateinit var adapter : DaysScheduleAdapter
    private var mapInitCheck = false
    private lateinit var data : Schedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)

        lifecycleScope.launch {
            val id = intent.getLongExtra(getString(R.string.server_calendar_id), -1)
            val idToLong = id.toLong()
            val latitude = intent.getDoubleExtra(getString(R.string.server_calendar_latitude), 0.0)
            val longitude = intent.getDoubleExtra(getString(R.string.server_calendar_longitude), 0.0)
            Log.d("onCreate", "id : ${id}t\nlatitude : ${latitude}\tlongitude : ${longitude}\nidToLong : $idToLong")
            viewModel.getScheduleDetail(this@CalendarActivity, MemberViewModel.tokensLiveData.value!!.accessToken, idToLong).collect()

            viewModel.scheduleLiveData.observe(this@CalendarActivity){schedule->
                data = schedule
                Log.d("onCreate", "getSchedule title : ${schedule.title}")
                binding.title.text = schedule.title
                val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(schedule.cost)
                binding.budget.text = getString(R.string.calendar_budget, formattedNumber)
                binding.date.text = "${schedule.startDate} - ${schedule.endDate}"

                val googleMapOptions = GoogleMapOptions()
                    .zoomControlsEnabled(true)

                supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);

                supportFragmentManager.beginTransaction()
                    .replace(R.id.map_card, supportMapFragment)
                    .commit()
                mapInitCheck = true

                if (schedule.dailySchedule.size != 0){
                    adapter = DaysScheduleAdapter(schedule.dailySchedule[selectedDays], this@CalendarActivity, this@CalendarActivity)
                    binding.daySchedules.adapter = adapter
                    binding.daysTitle.text = getString(R.string.days_title, (selectedDays+1).toString(), getDaysTitle(data, selectedDays+1))

                    supportMapFragment.getMapAsync(this@CalendarActivity)

                    setDaysTab(schedule)
                    setDaysTabItemMargin(schedule)
                    getDaysTitle(schedule, selectedDays)

                    binding.daysTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            val selectedTabPosition = binding.daysTab.selectedTabPosition
                            Log.d("OnTabSelected", "selectedTabPosition : $selectedTabPosition")
                            binding.daysTitle.text = getString(R.string.days_title, (selectedTabPosition + 1).toString(), getDaysTitle(data, selectedTabPosition))
                            lifecycleScope.launch {
                                adapter.update(data.dailySchedule[selectedTabPosition])
                                selectedDays = selectedTabPosition
                                if (mapInitCheck){
                                    supportMapFragment.getMapAsync(this@CalendarActivity)
                                }else{
                                    val googleMapOptions = GoogleMapOptions()
                                        .zoomControlsEnabled(true)

                                    supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);

                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.map_card, supportMapFragment)
                                        .commit()
                                    supportMapFragment.getMapAsync(this@CalendarActivity)
                                    mapInitCheck = true
                                }
                            }
                        }

                        override fun onTabUnselected(tab: TabLayout.Tab?) {

                        }

                        override fun onTabReselected(tab: TabLayout.Tab?) {

                        }

                    })
                }else{
                    supportMapFragment.getMapAsync {map ->
                        val location = LatLng(latitude, longitude)
                        val camera = EditScheduleBottomSheetDialog.makeScheduleCarmeraUpdate(location, 10f)
                        map.moveCamera(camera)
                    }
                }
            }
        }

    }

    private fun setDaysTab(data : Schedule){
        Log.d("setDaysTab", "data.dailySchedule.size : ${data.dailySchedule.size}")
        for(i in 1..data.dailySchedule.size){
            val tab = binding.daysTab.newTab().setText("${i}일차")
            binding.daysTab.addTab(tab)
        }
    }

    private fun setDaysTabItemMargin(data: Schedule){
        val tabs = binding.daysTab.getChildAt(0) as ViewGroup
        for(i in 0 until tabs.childCount){
            val tab = tabs.getChildAt(i)
            val lp = tab.layoutParams as LinearLayout.LayoutParams
            val dpPxChanger = DpPxChanger()
            lp.marginEnd = dpPxChanger.dpToPx(this, AiScheduleDialog.TAB_ITEM_MARGIN)
            lp.width = dpPxChanger.dpToPx(this, AiScheduleDialog.TAB_ITEM_WIDTH)
            lp.height = dpPxChanger.dpToPx(this, AiScheduleDialog.TAB_ITEM_HEIGHT)
            tab.layoutParams = lp
        }
        binding.daysTab.requestLayout()
    }

    private fun getDaysTitle(data: Schedule, tabNum : Int) : String{
        val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE)
        val formatter = DateTimeFormatter.ofPattern("MM.dd")
        return startDate.plusDays(tabNum.toLong()).format(formatter)
    }

    override fun onSearchButtonClickListener(data: DaysSchedule, position: Int) {

    }

    override fun onMapReady(map: GoogleMap) {
        map.clear()
        var first = true
        var polylineOptions = PolylineOptions()
        data.dailySchedule[selectedDays].forEachIndexed {position, data->
            if((data.latitude != null && data.longitude != null) || (data.longitude == 0.0 && data.latitude == 0.0)){
                val location = LatLng(data.latitude, data.longitude)
                if (first){
                    val camera = EditScheduleBottomSheetDialog.makeScheduleCarmeraUpdate(location, 10f)
                    map.moveCamera(camera)
                    first = false
                }
                polylineOptions.add(location)
                val markerOptions = EditScheduleBottomSheetDialog.makeScheduleMarkerOptions(this, data.type, position, location, data.places)
                map.addMarker(markerOptions)
            }
            map.addPolyline(polylineOptions)
        }
    }
}