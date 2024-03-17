package com.project.how.view.activity.calendar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.tabs.TabLayout
import com.project.how.R
import com.project.how.adapter.recyclerview.DaysScheduleEditAdapter
import com.project.how.adapter.item_touch_helper.RecyclerViewItemTouchHelperCallback
import com.project.how.adapter.recyclerview.AiDaysScheduleAdapter
import com.project.how.data_class.AiDaysSchedule
import com.project.how.data_class.AiSchedule
import com.project.how.data_class.DaysSchedule
import com.project.how.data_class.Schedule
import com.project.how.databinding.ActivityCalendarEditBinding
import com.project.how.interface_af.OnDateTimeListener
import com.project.how.interface_af.OnDesListener
import com.project.how.interface_af.OnScheduleListener
import com.project.how.interface_af.interface_ada.ItemStartDragListener
import com.project.how.view.dialog.AiScheduleDialog
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.bottom_sheet_dialog.CalendarBottomSheetDialog
import com.project.how.view.dialog.bottom_sheet_dialog.DesBottomSheetDialog
import com.project.how.view.dialog.bottom_sheet_dialog.EditScheduleBottomSheetDialog
import com.project.how.view.dp.DpPxChanger
import com.project.how.view_model.MemberViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch
import java.io.Serializable
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class CalendarEditActivity
    : AppCompatActivity(), OnMapReadyCallback, DaysScheduleEditAdapter.OnDaysButtonClickListener, OnScheduleListener, OnDesListener, OnDateTimeListener {
    private lateinit var binding : ActivityCalendarEditBinding
    private val viewModel : ScheduleViewModel by viewModels()
    private lateinit var data : Schedule
    private var type: Int = FAILURE
    private lateinit var adapter : DaysScheduleEditAdapter
    private lateinit var supportMapFragment: SupportMapFragment
    private var selectedDays = 0
    private var mapInitCheck = false
    private var id : Long = -1
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_edit)
        binding.edit = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            type = intent.getIntExtra(resources.getString(R.string.type), FAILURE)
            latitude = intent.getDoubleExtra(getString(R.string.server_calendar_latitude), 0.0)
            longitude = intent.getDoubleExtra(getString(R.string.server_calendar_longitude), 0.0)
            Log.d("CalendarEditActivity", "type : $type")
            getData(type)
        }

        viewModel.scheduleLiveData.observe(this){
            Log.d("CalendarEditActivity", "scheduleLiveData.observe start\n data.title : ${it.title}")
            data = it
            if (!mapInitCheck){
                binding.title.setText(data.title)
            }
            binding.date.text = "${data.startDate} - ${data.endDate}"
            val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.cost)
            binding.budget.text = getString(R.string.calendar_budget, formattedNumber)
            if (selectedDays > data.dailySchedule.lastIndex){
                selectedDays = data.dailySchedule.lastIndex
            }

            adapter = DaysScheduleEditAdapter(data.dailySchedule[selectedDays], this@CalendarEditActivity, this@CalendarEditActivity)
            binding.daySchedules.adapter = adapter

            binding.daysTab.removeAllTabs()
            binding.daysTitle.text = getString(R.string.days_title, (selectedDays+1).toString(), getDaysTitle( selectedDays+1))

            setDaysTab()
            setDaysTabItemMargin()

            val mCallback = RecyclerViewItemTouchHelperCallback(adapter)
            val mItemTouchHelper = ItemTouchHelper(mCallback)
            mItemTouchHelper.attachToRecyclerView(binding.daySchedules)

            val googleMapOptions = GoogleMapOptions()
                .zoomControlsEnabled(true)

            supportMapFragment = SupportMapFragment.newInstance(googleMapOptions);

            supportFragmentManager.beginTransaction()
                .replace(R.id.map_card, supportMapFragment)
                .commit()
            supportMapFragment.getMapAsync {map ->
                val location = LatLng(latitude, longitude)
                val camera = EditScheduleBottomSheetDialog.makeScheduleCarmeraUpdate(location, 10f)
                map.moveCamera(camera)
            }
            supportMapFragment.view?.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    MotionEvent.ACTION_UP -> {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    }
                }
                false
            }

            binding.scrollView.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
                false
            }

            adapter.itemDragListener(object : ItemStartDragListener {
                override fun onDropActivity(
                    initList: MutableList<DaysSchedule>,
                    changeList: MutableList<DaysSchedule>
                ) {
                    adapter.notifyDataSetChanged()
                    supportMapFragment.getMapAsync(this@CalendarEditActivity)
                }

            })
        }

        binding.daysTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTabPosition = binding.daysTab.selectedTabPosition
                Log.d("OnTabSelected", "selectedTabPosition : $selectedTabPosition")
                binding.daysTitle.text = getString(R.string.days_title, (selectedTabPosition + 1).toString(), getDaysTitle(selectedTabPosition))
                lifecycleScope.launch {
                    data.dailySchedule[selectedDays] = adapter.getData()
                    adapter.update(data.dailySchedule[selectedTabPosition])
                    selectedDays = selectedTabPosition
                    if (mapInitCheck){
                        supportMapFragment.getMapAsync(this@CalendarEditActivity)
                    }else{
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

    private fun setDaysTab(){
        Log.d("setDaysTab", "data.dailySchedule.size : ${data.dailySchedule.size}")
        for(i in 1..data.dailySchedule.size){
            val tab = binding.daysTab.newTab().setText("${i}일차")
            binding.daysTab.addTab(tab)
        }
    }

    private fun setDaysTabItemMargin(){
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

    private fun getDaysTitle(tabNum : Int) : String{
        val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE)
        val formatter = DateTimeFormatter.ofPattern("MM.dd")
        return startDate.plusDays(tabNum.toLong()).format(formatter)
    }

    private fun getData (type : Int){
        when(type){
            FAILURE ->{
                Toast.makeText(this, getString(R.string.get_data_failed), Toast.LENGTH_SHORT).show()
            }
            AI_SCHEDULE ->{
                viewModel.getSchedule(getSerializable(this, resources.getString(R.string.aischedule), AiSchedule::class.java))
            }
            NEW ->{
                viewModel.getSchedule(getSerializable(this, getString(R.string.schedule), Schedule::class.java))
            }
            EDIT ->{
                viewModel.getSchedule(getSerializable(this, getString(R.string.schedule), Schedule::class.java))
                id = intent.getLongExtra(getString(R.string.server_calendar_id), -1)
            }
        }
    }

    fun add(){
        val newData = DaysSchedule(
            AiDaysScheduleAdapter.AIRPLANE,
            "",
            "",
            null,
            null,
            0.toLong(),
            false,
            null
        )
        data.dailySchedule[selectedDays].add(newData)
        val editScheduleBottomSheet = EditScheduleBottomSheetDialog(newData , data.dailySchedule[selectedDays].lastIndex, this)
        editScheduleBottomSheet.show(supportFragmentManager, "EditScheduleBottomSheetDialog")
    }

    fun save(){
        lifecycleScope.launch {
            data.title = binding.title.text.toString()
            data.dailySchedule[selectedDays] = adapter.getData()
            viewModel.getSchedule(data)

            when(type){
                NEW->{
                    saveNewSchedule()
                }
                AI_SCHEDULE->{
                    saveNewSchedule()
                }
                EDIT->{
                    saveEditSchedule()
                }
                FAILURE->{
                    Toast.makeText(this@CalendarEditActivity, getString(R.string.get_data_failed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showEditDes(){
        val des = DesBottomSheetDialog(this)
        des.show(supportFragmentManager, "DesBottomSheetDialog")
    }

    fun showEditDeparture(){
        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.DEPARTURE, this)
        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
    }

    fun showEditEntrance(){
        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.ENTRANCE, this)
        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
    }

    private suspend fun saveNewSchedule(){

        viewModel.saveSchedule(this, MemberViewModel.tokensLiveData.value!!.accessToken, data).collect{check ->
            when(check){
                ScheduleViewModel.NULL_LOCATIONS ->{
                    val message = listOf<String>(getString(R.string.some_schedule_lng_lat))
                    val confirmDialog = ConfirmDialog(message)
                    confirmDialog.show(supportFragmentManager, "ConfirmDialog")
                }
                ScheduleViewModel.NETWORK_FAILED ->{
                    Toast.makeText(this@CalendarEditActivity,
                        getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
                ScheduleViewModel.SUCCESS ->{
                    moveCalendarList()
                }
            }
        }
    }

    private suspend fun saveEditSchedule(){
        viewModel.updateSchedule(this, MemberViewModel.tokensLiveData.value!!.accessToken, id, data).collect{check->
            when(check){
                ScheduleViewModel.NULL_LOCATIONS ->{
                    val message = listOf<String>(getString(R.string.some_schedule_lng_lat))
                    val confirmDialog = ConfirmDialog(message)
                    confirmDialog.show(supportFragmentManager, "ConfirmDialog")
                }
                ScheduleViewModel.NETWORK_FAILED ->{
                    Toast.makeText(this@CalendarEditActivity,
                        getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
                ScheduleViewModel.EMPTY_SCHEDULE->{
                    Toast.makeText(this@CalendarEditActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
                ScheduleViewModel.SUCCESS->{
                    moveCalendar()
                }
            }
        }
    }

    private fun moveCalendarList(){
        val intent = Intent(this, CalendarListActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveCalendar(){
        val intent = Intent(this, CalendarActivity::class.java)
        intent.putExtra(getString(R.string.server_calendar_id), id)
        intent.putExtra(getString(R.string.server_calendar_latitude), latitude)
        intent.putExtra(getString(R.string.server_calendar_longitude), longitude)
        startActivity(intent)
        finish()
    }

    private fun setBudgetText(totalCost : Long){
        data.cost = totalCost
        val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.cost)
        binding.budget.text = getString(R.string.calendar_budget, formattedNumber)
    }

    override fun onEditButtonClickListener(data : DaysSchedule, position : Int) {
        val editScheduleBottomSheet = EditScheduleBottomSheetDialog(data , position, this)
        editScheduleBottomSheet.show(supportFragmentManager, "EditScheduleBottomSheetDialog")
    }

    override fun onDeleteButtonClickListener(position: Int) {
        data.dailySchedule[selectedDays]= adapter.getData()
        lifecycleScope.launch {
            viewModel.getTotalCost(data).collect{
                setBudgetText(it)
            }
        }
    }

    override fun onDaysScheduleListener(schedule: DaysSchedule, position: Int) {
        data.dailySchedule[selectedDays][position] = schedule
        adapter.edit(schedule, position)
        lifecycleScope.launch {
            viewModel.getTotalCost(data).collect {
                setBudgetText(it)
            }
        }
        supportMapFragment.getMapAsync(this)
    }

    override fun onDesListener(des: String) {
        data.country = des
    }

    override fun onSaveDate(date: String, type: Int) {
        when(type){
            CalendarBottomSheetDialog.BASIC ->{

            }
            CalendarBottomSheetDialog.DEPARTURE->{
                if(data.endDate >= date){
                    data.startDate = date
                    binding.date.text = "${data.startDate} - ${data.endDate}"
                    viewModel.updateDailySchedule(data, data.startDate, data.endDate)
                }else{
                    Toast.makeText(this, "출국 날짜($date)보다 입국 날짜(${data.endDate})가 더 빠릅니다.", Toast.LENGTH_SHORT).show()
                }
            }
            CalendarBottomSheetDialog.ENTRANCE->{
                if (data.startDate <= date){
                    data.endDate = date
                    binding.date.text = "${data.startDate} - ${data.endDate}"
                    viewModel.updateDailySchedule(data, data.startDate, data.endDate)
                }else{
                    Toast.makeText(this, "입국 날짜($date)보다 출국 날짜(${data.startDate})가 더 늦습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveDateTime(dateTime: String, type: Int) {

    }

    companion object{
        const val FAILURE = -1
        const val AI_SCHEDULE = 0
        const val NEW = 1
        const val EDIT = 2

        fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
        {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                activity.intent.getSerializableExtra(name, clazz)!!
            else
                activity.intent.getSerializableExtra(name) as T
        }
    }

}