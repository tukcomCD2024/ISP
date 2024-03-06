package com.project.how.view.activity.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.model.LatLng
import com.project.how.R
import com.project.how.adapter.recyclerview.CalendarListAdapter
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

    override fun onItemClickListener(id: Long, latLng: LatLng) {

    }

    override fun onCheckListButtonClickListener(id: Long) {

    }

    override fun onShareButtonClickListener(id: Long) {

    }

    override fun onScheduleDeleteListener(position: Int) {
        val data = adapter.getData(position)
        viewModel.deleteSchedule(this, MemberViewModel.tokensLiveData.value!!.accessToken, data.id)
        adapter.remove(position)
    }

    override fun onAddressCheckListener() {
        TODO("Not yet implemented")
    }

    override fun onKeepCheckListener() {
        TODO("Not yet implemented")
    }
}