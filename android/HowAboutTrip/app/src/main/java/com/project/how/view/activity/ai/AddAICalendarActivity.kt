package com.project.how.view.activity.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.project.how.R
import com.project.how.data_class.recyclerview.AiSchedule
import com.project.how.data_class.AiScheduleInput
import com.project.how.data_class.AiScheduleListInput
import com.project.how.data_class.dto.GetCountryLocationResponse
import com.project.how.data_class.recyclerview.AiScheduleList
import com.project.how.databinding.ActivityAddAicalendarBinding
import com.project.how.interface_af.OnAddListener
import com.project.how.interface_af.OnDesListener
import com.project.how.interface_af.OnPurposeListener
import com.project.how.interface_af.OnActivityListener
import com.project.how.interface_af.OnDialogListener
import com.project.how.view.activity.calendar.CalendarEditActivity
import com.project.how.view.dialog.AiScheduleDialog
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.WarningDialog
import com.project.how.view.dialog.bottom_sheet_dialog.DesBottomSheetDialog
import com.project.how.view.dialog.bottom_sheet_dialog.PurposeBottomSheetDialog
import com.project.how.view.dialog.bottom_sheet_dialog.ActivityBottomSheetDialog
import com.project.how.view_model.AiScheduleViewModel
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class AddAICalendarActivity :
    AppCompatActivity(), OnDesListener, OnPurposeListener, OnAddListener, OnActivityListener, OnDialogListener {
    private lateinit var binding : ActivityAddAicalendarBinding
    private val viewModel : AiScheduleViewModel by viewModels()
    private val scheduleViewModel : ScheduleViewModel by viewModels()
    private var destination : String? = null
    private var purpose : MutableList<String>? = null
    private var activities : MutableList<String>? = null
    private var excludingActivities : MutableList<String>? = null
    private var departureDate : String? = null
    private var entranceDate : String? = null
    private var latLng : GetCountryLocationResponse? = null
    private lateinit var aiSchedule : AiSchedule
    private lateinit var aiScheduleList : ArrayList<AiSchedule>

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

        viewModel.aiScheduleListLiveData.observe(this){
            Log.d("aiScheduleListLiveData", "${it[0].startDate} - ${it[0].endDate}")
            setEnabled()
            stopLoaing()
            aiScheduleList = ArrayList(it)
            moveAiScheduleList()
        }

    }

//    fun showDepartureInput(){
//        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.DEPARTURE, this)
//        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
//    }
//
//    fun showEntranceInput(){
//        val calendar = CalendarBottomSheetDialog(CalendarBottomSheetDialog.ENTRANCE, this)
//        calendar.show(supportFragmentManager, "CalendarBottomSheetDialog")
//    }

    fun showDesInput(){
        val des = DesBottomSheetDialog(this)
        des.show(supportFragmentManager, "DesBottomSheetDialog")
    }

    fun showPurposeInput(){
        purpose = mutableListOf()
        binding.themeOutput.text = ""
        val purpose = PurposeBottomSheetDialog(this)
        purpose.show(supportFragmentManager, "PurposeBottomSheetDialog")
    }

    fun showActivityInput(){
        activities = mutableListOf()
        binding.activityOutput.text = ""
        val activity = ActivityBottomSheetDialog(this, ActivityBottomSheetDialog.BASIC)
        activity.show(supportFragmentManager, "ActivityBottomSheetDialog")
    }

    fun showExcludingActivityInput(){
        excludingActivities = mutableListOf()
        binding.excludingActivityOutput.text = ""
        val activity = ActivityBottomSheetDialog(this, ActivityBottomSheetDialog.EXCLUDING)
        activity.show(supportFragmentManager, "ExcludingActivityBottomSheetDialog")
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
        calendar.show(supportFragmentManager, "MaterialDatePicker")

        calendar.addOnPositiveButtonClickListener {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it.first
            val format = SimpleDateFormat("yyyy-MM-dd")
            val formatted = format.format(utc.time)
            utc.timeInMillis = it.second
            val formattedSecond = format.format(utc.time)
            entranceDate = formattedSecond
            departureDate = formatted
            binding.dateOutput.text = getString(R.string.date_text, formatted, formattedSecond)
        }
    }

    fun searchList(){
        lifecycleScope.launch {
            if ((destination != null) && (departureDate != null) && (entranceDate != null)){
                Log.d("aiScheduleLiveData", "start ${destination}, ${departureDate}, ${entranceDate}")
                setUnEnabled()
                load()
                viewModel.getAiScheduleList(AiScheduleListInput(destination!!, purpose, activities, excludingActivities, departureDate!!, entranceDate!!)).collect { check ->
                    if (!check){
                        stopLoaing()
                        setEnabled()
                        Toast.makeText(this@AddAICalendarActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
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
        binding.themeInput.isEnabled = false
        binding.excludingActivityInput.isEnabled = false
        binding.dateInput.isEnabled = false
    }

    private fun setEnabled(){
        binding.search.isEnabled = true
        binding.desInput.isEnabled = true
        binding.themeInput.isEnabled = true
        binding.excludingActivityInput.isEnabled = true
        binding.dateInput.isEnabled = true
    }

    private fun moveAiScheduleList(){
        val intent = Intent(this, AiScheduleListActivity::class.java)
        intent.putExtra(getString(R.string.aischedule), AiScheduleList(aiScheduleList))
        intent.putExtra(getString(R.string.server_calendar_latitude), latLng?.lat)
        intent.putExtra(getString(R.string.server_calendar_longitude), latLng?.lng)
        startActivity(intent)
    }
    private fun showAiSchedule(data : AiSchedule){
        val scheduleDialog = AiScheduleDialog(data, this)
        scheduleDialog.show(supportFragmentManager, "AiScheduleDialog")
    }

    private fun showConfirmDialog(message : MutableList<String>){
        val confirm = ConfirmDialog(message)
        confirm.show(supportFragmentManager, "ConfirmDialog")
    }

    fun showWarningDialog(){
        val warning = WarningDialog(getString(R.string.ai_date_range_warning), this)
        warning.show(supportFragmentManager, "WarningDialog")
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
        binding.themeOutput.visibility = View.VISIBLE
        binding.themeOutput.text = binding.themeOutput.text.toString() + who +", "
    }

    override fun onActivityLevelListener(activityLevel: String) {
        purpose?.add(activityLevel)
        binding.themeOutput.visibility = View.VISIBLE
        binding.themeOutput.text = binding.themeOutput.text.toString() + activityLevel + ", "
    }

    override fun onPurposeListener(purpose: List<String>) {
        purpose.forEachIndexed { index, s ->
            this.purpose?.add(s)
            if (index == purpose.lastIndex){
                binding.themeOutput.text = binding.themeOutput.text.toString() + s
            }else{
                binding.themeOutput.text = binding.themeOutput.text.toString() + s + ", "
            }
        }
    }

    override fun onActivityListener(theme: List<String>, type : Int) {
        lifecycleScope.launch {
            if (type == ActivityBottomSheetDialog.BASIC){
                theme.forEachIndexed{ index, s ->
                    this@AddAICalendarActivity.activities?.add(s)
                    if (index == theme.lastIndex)
                        binding.activityOutput.text = binding.activityOutput.text.toString() + s
                    else
                        binding.activityOutput.text = binding.activityOutput.text.toString() + s + ", "
                }
            }else{
                theme.forEachIndexed{ index, s ->
                    this@AddAICalendarActivity.excludingActivities?.add(s)
                    if (index == theme.lastIndex)
                        binding.excludingActivityOutput.text = binding.excludingActivityOutput.text.toString() + s
                    else
                        binding.excludingActivityOutput.text = binding.excludingActivityOutput.text.toString() + s + ", "
                }
            }
        }
    }

    override fun onTransportationListener(transportation: List<String>) {
        transportation.forEachIndexed { index, s ->
            purpose?.add(s)
            if (index == transportation.lastIndex){
                binding.themeOutput.text = binding.themeOutput.text.toString() + s
            }else{
                binding.themeOutput.text = binding.themeOutput.text.toString() + s + ", "
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

    override fun onDismissListener() {
        showCalendar()
    }
}