package com.project.how.view.activity.calendar

import android.content.Intent
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
import com.project.how.adapter.recyclerview.schedule.DaysScheduleAdapter
import com.project.how.data_class.recyclerview.schedule.DaysSchedule
import com.project.how.data_class.recyclerview.schedule.Schedule
import com.project.how.databinding.ActivityCalendarBinding
import com.project.how.view.dialog.AiScheduleDialog
import com.project.how.view.dialog.ChecklistDialog
import com.project.how.view.dp.DpPxChanger
import com.project.how.view.map_helper.CameraOptionProducer
import com.project.how.view.map_helper.MarkerProducer
import com.project.how.view_model.CountryViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarActivity : AppCompatActivity(), DaysScheduleAdapter.OnDaysButtonClickListener, OnMapReadyCallback {
    private lateinit var binding : ActivityCalendarBinding
    private val viewModel : ScheduleViewModel by viewModels()
    private val countryViewModel : CountryViewModel by viewModels()
    private lateinit var supportMapFragment: SupportMapFragment
    private var selectedDays = 0
    private lateinit var adapter : DaysScheduleAdapter
    private var mapInitCheck = false
    private lateinit var data : Schedule
    private var idToLong: Long = -1
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var id = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        binding.calendar = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            init()

            viewModel.scheduleLiveData.observe(this@CalendarActivity){schedule->
                data = schedule
                if (latitude == 0.0 && longitude == 0.0)
                    getLngLat()
                Log.d("onCreate", "getSchedule title : ${schedule.title}")

                setCalendarTopUI()
                mapInit()

                if (schedule.dailySchedule.size != 0){
                    adapter = DaysScheduleAdapter(schedule.dailySchedule[selectedDays], this@CalendarActivity, data.currency,this@CalendarActivity)
                    binding.daySchedules.adapter = adapter
                    binding.daysTitle.text = getString(R.string.days_title, (selectedDays+1).toString(), getDaysTitle(data, selectedDays))

                    supportMapFragment.getMapAsync(this@CalendarActivity)

                    setDaysTab()
                }
            }
        }

    }

    private fun mapInit(){
        val googleMapOptions = GoogleMapOptions()
            .zoomControlsEnabled(true)

        supportMapFragment = SupportMapFragment.newInstance(googleMapOptions)

        supportFragmentManager.beginTransaction()
            .replace(R.id.map_card, supportMapFragment)
            .commit()
        mapInitCheck = true

        supportMapFragment.getMapAsync {map ->
            val location = LatLng(latitude, longitude)
            val camera = CameraOptionProducer().makeScheduleCameraUpdate(location, 10f)
            map.moveCamera(camera)
        }
    }

    private fun setCalendarTopUI(){
        binding.title.text = data.title
        val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.cost)
        binding.budget.text = getString(R.string.calendar_budget, formattedNumber, data.currency)
        binding.date.text = "${data.startDate} - ${data.endDate}"
    }

    private fun setDaysTab(){
        setDaysTab(data)
        setDaysTabItemMargin(data)

        binding.daysTab.clearOnTabSelectedListeners()
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

                        supportMapFragment = SupportMapFragment.newInstance(googleMapOptions)

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

    fun checklist(){
        val checkList = ChecklistDialog(id).show(supportFragmentManager, "CheckListDialog")
    }

    fun delete(){
        lifecycleScope.launch {
            viewModel.deleteSchedule(idToLong).collect{
                when(it){
                    ScheduleViewModel.SUCCESS -> {
                        startActivity(Intent(this@CalendarActivity, CalendarListActivity::class.java))
                        finish()
                    }
                    ScheduleViewModel.OTHER_USER_SCHEDULE -> {
                        Toast.makeText(this@CalendarActivity, getString(R.string.other_user_schedule_error), Toast.LENGTH_SHORT).show()
                    }
                    ScheduleViewModel.NOT_EXIST_SCHEDULE -> {
                        Toast.makeText(this@CalendarActivity, getString(R.string.not_exist_schedule_error), Toast.LENGTH_SHORT).show()
                    }
                    ScheduleViewModel.NETWORK_FAILED -> {
                        Toast.makeText(this@CalendarActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private suspend fun init(){
        id = intent.getLongExtra(getString(R.string.server_calendar_id), -1)
        idToLong = id.toLong()
        latitude = intent.getDoubleExtra(getString(R.string.server_calendar_latitude), 0.0)
        longitude = intent.getDoubleExtra(getString(R.string.server_calendar_longitude), 0.0)

        Log.d("onCreate", "id : ${id}t\nlatitude : ${latitude}\tlongitude : ${longitude}\nidToLong : $idToLong")
        viewModel.getScheduleDetail(this@CalendarActivity, idToLong).collect{ check->
            if(check != ScheduleViewModel.SUCCESS) {
                Toast.makeText(
                    this@CalendarActivity,
                    getString(R.string.not_exist_schedule_error),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    fun edit(){
        val intent = Intent(this, CalendarEditActivity::class.java)
        intent.putExtra(getString(R.string.type), CalendarEditActivity.EDIT)
        intent.putExtra(getString(R.string.schedule), data)
        intent.putExtra(getString(R.string.server_calendar_id), idToLong)
        intent.putExtra(getString(R.string.server_calendar_latitude), latitude)
        intent.putExtra(getString(R.string.server_calendar_longitude), longitude)
        startActivity(intent)
        finish()
    }

    override fun onSearchButtonClickListener(data: DaysSchedule, position: Int) {

    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch {
            map.clear()
            val polylineOptions = PolylineOptions()
            val latitudes = mutableListOf<Double>()
            val longitudes = mutableListOf<Double>()

            data.dailySchedule[selectedDays].forEachIndexed {position, data->
                if((data.latitude != null && data.longitude != null) && (data.longitude != 0.0 && data.latitude != 0.0)){
                    val location = LatLng(data.latitude, data.longitude)
                    latitudes.add(data.latitude)
                    longitudes.add(data.longitude)
                    polylineOptions.add(location)
                    val markerOptions = MarkerProducer().makeScheduleMarkerOptions(this@CalendarActivity, data.type, position, location, data.places)
                    map.addMarker(markerOptions)
                }
            }
            map.addPolyline(polylineOptions)

            if(latitudes.lastIndex == 0){
                val cop = CameraOptionProducer()
                val camera = cop.makeScheduleCameraUpdate(LatLng(latitudes[0], longitudes[0]), 12f)
                map.moveCamera(camera)
            }else if (latitudes.isNotEmpty()){
                val cop = CameraOptionProducer()
                val locations = cop.makeLatLngBounds(latitudes, longitudes)
                val camera = cop.makeScheduleBoundsCameraUpdate(locations[0], locations[1], 120)
                map.moveCamera(camera)
            }else{
                val location = LatLng(latitude, longitude)
                val camera = CameraOptionProducer().makeScheduleCameraUpdate(location, 10f)
                map.moveCamera(camera)
            }
        }
    }

    private fun getLngLat(){
        lifecycleScope.launch {
            countryViewModel.getCountryLocation(data.country).collect{ location->
                location?.let {
                    longitude = location.lng
                    latitude = location.lat
                } ?: Toast.makeText(this@CalendarActivity, getString(R.string.location_load_error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}