package com.project.how.view.activity.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.data_class.AiSchedule
import com.project.how.data_class.AiScheduleInput
import com.project.how.data_class.dto.GetCountryLocationResponse
import com.project.how.databinding.ActivityAddAicalendarBinding
import com.project.how.interface_af.OnAddListener
import com.project.how.interface_af.OnDateTimeListener
import com.project.how.interface_af.OnDesListener
import com.project.how.interface_af.OnPurposeListener
import com.project.how.view.activity.calendar.CalendarEditActivity
import com.project.how.view.dialog.AiScheduleDialog
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.bottom_sheet_dialog.CalendarBottomSheetDialog
import com.project.how.view.dialog.bottom_sheet_dialog.DesBottomSheetDialog
import com.project.how.view.dialog.bottom_sheet_dialog.PurposeBottomSheetDialog
import com.project.how.view_model.AiScheduleViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch

class AddAICalendarActivity :
    AppCompatActivity(), OnDateTimeListener, OnDesListener, OnPurposeListener, OnAddListener {
    private lateinit var binding : ActivityAddAicalendarBinding
    private val viewModel : AiScheduleViewModel by viewModels()
    private val scheduleViewModel : ScheduleViewModel by viewModels()
    private var destination : String? = null
    private var purpose : MutableList<String>? = null
    private var departureDate : String? = null
    private var entranceDate : String? = null
    private var latLng : GetCountryLocationResponse? = null
    private lateinit var aiSchedule : AiSchedule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_aicalendar)
        binding.ai = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            MobileAds.initialize(this@AddAICalendarActivity)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }

        viewModel.aiScheduleLiveData.observe(this){
            if(it.startDate == departureDate && it.endDate == entranceDate){
                Log.d("aiScheduleLiveData", "${it.startDate} - ${it.endDate}")
                setEnabled()
                stopLoaing()
                aiSchedule = it
                showAiSchedule(it)
            }
        }

    }

    fun showDepartureInput(){
        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.DEPARTURE, this)
        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
    }

    fun showEntranceInput(){
        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.ENTRANCE, this)
        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
    }

    fun showDesInput(){
        val des = DesBottomSheetDialog(this)
        des.show(supportFragmentManager, "DesBottomSheetDialog")
    }

    fun showPurposeInput(){
        purpose = mutableListOf()
        binding.purposeOutput.text = ""
        val purpose = PurposeBottomSheetDialog(this)
        purpose.show(supportFragmentManager, "PurposeBottomSheetDialog")
    }

    fun search(){
        lifecycleScope.launch {
            if ((destination != null) && (departureDate != null) && (entranceDate != null)){
                Log.d("aiScheduleLiveData", "start ${destination}, ${departureDate}, ${entranceDate}")
                setUnEnabled()
                load()
                viewModel.getAiSchedule(AiScheduleInput(destination!!, purpose, departureDate!!, entranceDate!!), false)
            }else{
                val message = mutableListOf<String>()
                if (destination == null)
                    message.add(resources.getString(R.string.destination))
                if (departureDate == null)
                    message.add(resources.getString(R.string.departure_date))
                if (entranceDate == null)
                    message.add(resources.getString(R.string.entrance_date))

                showConfirmDialog(message)
            }
        }
    }

    private fun load(){
        binding.loadingBackground.visibility = View.VISIBLE
        binding.loadingInfo.visibility = View.VISIBLE
        binding.loadingLottie.visibility = View.VISIBLE
        binding.loadingLottie.playAnimation()
    }

    private fun stopLoaing(){
        binding.loadingBackground.visibility = View.GONE
        binding.loadingInfo.visibility = View.GONE
        binding.loadingLottie.visibility = View.GONE
        binding.loadingLottie.pauseAnimation()
    }

    private fun setUnEnabled(){
        binding.search.isEnabled = false
        binding.desInput.isEnabled = false
        binding.purposeInput.isEnabled = false
        binding.departureInput.isEnabled = false
        binding.entranceInput.isEnabled = false
    }

    private fun setEnabled(){
        binding.search.isEnabled = true
        binding.desInput.isEnabled = true
        binding.purposeInput.isEnabled = true
        binding.departureInput.isEnabled = true
        binding.entranceInput.isEnabled = true
    }

    private fun moveAiScheduleList(){
        val intent = Intent(this, AiScheduleListActivity::class.java)
        startActivity(intent)
    }
    private fun showAiSchedule(data : AiSchedule){
        val scheduleDialog = AiScheduleDialog(data, this)
        scheduleDialog.show(supportFragmentManager, "AiScheduleDialog")
    }

    private fun saveDepartureDate(date : String){
        departureDate = date
        binding.departureOutput.text = date
        binding.departureOutput.visibility = View.VISIBLE
    }

    private fun saveEntranceDate(date : String){
        entranceDate = date
        binding.entranceOutput.text = date
        binding.entranceOutput.visibility = View.VISIBLE
    }

    private fun showConfirmDialog(message : MutableList<String>){
        val confirm = ConfirmDialog(message)
        confirm.show(supportFragmentManager, "ConfirmDialog")
    }

    override fun onSaveDate(date: String, type: Int) {
        when(type){
            CalendarBottomSheetDialog.BASIC ->{

            }
            CalendarBottomSheetDialog.DEPARTURE->{
                if (entranceDate == null){
                    saveDepartureDate(date)
                }else{
                    if(entranceDate!! >= date){
                        saveDepartureDate(date)
                    }else{
                        Toast.makeText(this, "출국 날짜($date)보다 입국 날짜($entranceDate)가 더 빠릅니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            CalendarBottomSheetDialog.ENTRANCE->{
                if (departureDate == null){
                    saveEntranceDate(date)
                }else{
                    if (departureDate!! <= date){
                        saveEntranceDate(date)
                    }else{
                        Toast.makeText(this, "입국 날짜($date)보다 출국 날짜($departureDate)가 더 늦습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onSaveDateTime(dateTime: String, type: Int) {
        if(type == CalendarBottomSheetDialog.ENTRANCE){
            binding.entranceOutput.text = dateTime
            binding.entranceOutput.visibility = View.VISIBLE
        }else if(type == CalendarBottomSheetDialog.DEPARTURE){
            binding.departureOutput.text = dateTime
            binding.departureOutput.visibility = View.VISIBLE
        }
    }

    override fun onDesListener(des: String) {
        lifecycleScope.launch {
            scheduleViewModel.getCountryLocation(des).collect{ location ->
                location?.let {
                    destination = des
                    binding.desOutput.text = des
                    binding.desOutput.visibility = View.VISIBLE
                    latLng = location
                } ?: run {
                    scheduleViewModel.getCountryLocation(des).collect { newLocation ->
                        newLocation?.let {
                            destination = des
                            binding.desOutput.text = des
                            binding.desOutput.visibility = View.VISIBLE
                            latLng = newLocation
                        } ?: run {
                            Toast.makeText(this@AddAICalendarActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onWhoListener(who: String) {
        purpose?.add(who)
        binding.purposeOutput.visibility = View.VISIBLE
        binding.purposeOutput.text = binding.purposeOutput.text.toString() + who +", "
    }

    override fun onActivityLevelListener(activityLevel: String) {
        purpose?.add(activityLevel)
        binding.purposeOutput.visibility = View.VISIBLE
        binding.purposeOutput.text = binding.purposeOutput.text.toString() + activityLevel + ", "
    }

    override fun onThemeListener(theme: List<String>) {
        binding.purposeOutput.visibility = View.VISIBLE
        theme.forEachIndexed { index, s ->
            purpose?.add(s)
            if (index == theme.lastIndex){
                binding.purposeOutput.text = binding.purposeOutput.text.toString() + s
            }else{
                binding.purposeOutput.text = binding.purposeOutput.text.toString() + s + ", "
            }
        }
    }

    override fun onAddListener() {
        val intent = Intent(this, CalendarEditActivity::class.java)
        intent.putExtra(getString(R.string.type), CalendarEditActivity.AI_SCHEDULE)
        intent.putExtra(getString(R.string.aischedule), aiSchedule)
        intent.putExtra(getString(R.string.server_calendar_latitude), latLng!!.lat)
        intent.putExtra(getString(R.string.server_calendar_longitude), latLng!!.lng)
        startActivity(intent)
        finish()
    }
}