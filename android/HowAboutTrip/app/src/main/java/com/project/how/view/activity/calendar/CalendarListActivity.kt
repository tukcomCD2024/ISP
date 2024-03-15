package com.project.how.view.activity.calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.project.how.R
import com.project.how.adapter.recyclerview.CalendarListAdapter
import com.project.how.data_class.Schedule
import com.project.how.data_class.ScheduleIDAndLatLng
import com.project.how.data_class.dto.GetScheduleListResponseElement
import com.project.how.databinding.ActivityCalendarListBinding
import com.project.how.interface_af.OnDateTimeListener
import com.project.how.interface_af.OnDesListener
import com.project.how.interface_af.OnYesOrNoListener
import com.project.how.view.dialog.YesOrNoDialog
import com.project.how.view.dialog.bottom_sheet_dialog.CalendarBottomSheetDialog
import com.project.how.view.dialog.bottom_sheet_dialog.DesBottomSheetDialog
import com.project.how.view_model.MemberViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch

class CalendarListActivity
    : AppCompatActivity(), CalendarListAdapter.OnCalendarListButtonClickListener, OnYesOrNoListener, OnDateTimeListener, OnDesListener {
    private lateinit var binding : ActivityCalendarListBinding
    private lateinit var adapter : CalendarListAdapter
    private val viewModel : ScheduleViewModel by viewModels()
    private var destination : String? = null
    private var departureDate : String? = null
    private var entranceDate : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_list)
        binding.list = this
        binding.lifecycleOwner = this

        viewModel.scheduleListLiveData.observe(this@CalendarListActivity){
            adapter = CalendarListAdapter(this@CalendarListActivity, it, this@CalendarListActivity)
            binding.calendarList.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.getScheduleList(this@CalendarListActivity, MemberViewModel.tokensLiveData.value!!.accessToken)
        }
    }

    fun add() {
        showDesInput()
    }

    private fun showDepartureInput(){
        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.DEPARTURE, this)
        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
    }

    private fun showEntranceInput(){
        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.ENTRANCE, this)
        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
    }

    private fun showDesInput(){
        val des = DesBottomSheetDialog(this)
        des.show(supportFragmentManager, "DesBottomSheetDialog")
    }

    override fun onDeleteButtonClickListener(data : GetScheduleListResponseElement, position : Int) {
        val yesOrNoDialog = YesOrNoDialog(data.scheduleName, YesOrNoDialog.SCHEDULE_DELETE, position, this)
        yesOrNoDialog.show(supportFragmentManager, "YesOrNoDialog")
    }

    override fun onItemClickListener(id: Long, latitude : Double, longitude : Double) {
        Log.d("onCreate", "onItemClickerListener\nid : ${id}\nlatitude : ${latitude}\tlongitude : ${longitude}")
        val intent = Intent(this, CalendarActivity::class.java)
        intent.putExtra(getString(R.string.server_calendar_id), id)
        intent.putExtra(getString(R.string.server_calendar_latitude), latitude)
        intent.putExtra(getString(R.string.server_calendar_longitude), longitude)
        startActivity(intent)
        finish()
    }

    override fun onCheckListButtonClickListener(id: Long) {

    }

    override fun onShareButtonClickListener(id: Long) {

    }

    override fun onScheduleDeleteListener(position: Int) {
       lifecycleScope.launch {
           val data = adapter.getData(position)
           viewModel.deleteSchedule(this@CalendarListActivity, MemberViewModel.tokensLiveData.value!!.accessToken, data.id).collect{
                when(it){
                    ScheduleViewModel.SUCCESS -> { adapter.remove(position) }
                    ScheduleViewModel.OTHER_USER_SCHEDULE -> { Toast.makeText(this@CalendarListActivity, getString(R.string.other_user_schedule_delete_error), Toast.LENGTH_SHORT).show()}
                    ScheduleViewModel.NETWORK_FAILED -> { Toast.makeText(this@CalendarListActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show() }
                    ScheduleViewModel.NOT_EXIST_SCHEDULE -> {Toast.makeText(this@CalendarListActivity, getString(R.string.not_exist_schedule_delete_error), Toast.LENGTH_SHORT).show() }
                }
           }
       }
    }

    override fun onAddressCheckListener() {
        TODO("Not yet implemented")
    }

    override fun onKeepCheckListener() {
        TODO("Not yet implemented")
    }

    override fun onSaveDate(date: String, type: Int) {
        if(type == CalendarBottomSheetDialog.ENTRANCE){
            if(date < departureDate!!){
                Toast.makeText(this, "입국 날짜($date)보다 출국 날짜($departureDate)가 더 늦습니다.", Toast.LENGTH_SHORT).show()
                showEntranceInput()
            }else{
                entranceDate = date
                val intent = Intent(this, CalendarEditActivity::class.java)
                val schedule = Schedule(
                    destination!!,
                    destination!!,
                    departureDate!!,
                    entranceDate!!,
                    0,
                    viewModel.getEmptyDaysSchedule(departureDate!!, entranceDate!!)
                )
                intent.putExtra(getString(R.string.type), CalendarEditActivity.NEW)
                intent.putExtra(getString(R.string.schedule), schedule)
                startActivity(intent)
                finish()
            }

        }else if(type == CalendarBottomSheetDialog.DEPARTURE){
            departureDate = date
            showEntranceInput()
        }
    }

    override fun onSaveDateTime(dateTime: String, type: Int) {
        TODO("Not yet implemented")
    }

    override fun onDesListener(des: String) {
        destination = des
        showDepartureInput()
    }
}