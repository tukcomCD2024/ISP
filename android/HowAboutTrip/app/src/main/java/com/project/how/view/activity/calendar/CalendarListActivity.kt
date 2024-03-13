package com.project.how.view.activity.calendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.project.how.R
import com.project.how.adapter.recyclerview.CalendarListAdapter
import com.project.how.data_class.ScheduleIDAndLatLng
import com.project.how.data_class.dto.GetScheduleListResponseElement
import com.project.how.databinding.ActivityCalendarListBinding
import com.project.how.interface_af.OnYesOrNoListener
import com.project.how.view.dialog.YesOrNoDialog
import com.project.how.view_model.MemberViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch

class CalendarListActivity : AppCompatActivity(), CalendarListAdapter.OnCalendarListButtonClickListener, OnYesOrNoListener {
    private lateinit var binding : ActivityCalendarListBinding
    private lateinit var adapter : CalendarListAdapter
    private val viewModel : ScheduleViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar_list)
        binding.list = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            viewModel.getScheduleList(this@CalendarListActivity, MemberViewModel.tokensLiveData.value!!.accessToken)

            viewModel.scheduleListLiveData.observe(this@CalendarListActivity){
                adapter = CalendarListAdapter(this@CalendarListActivity, it, this@CalendarListActivity)
                binding.calendarList.adapter = adapter
            }
        }
    }

    fun add(){

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
}