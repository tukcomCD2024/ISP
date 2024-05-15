package com.project.how.view.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.project.how.R
import com.project.how.data_class.dto.GetFlightOffersRequest
import com.project.how.databinding.FragmentOneWaySearchBinding
import com.project.how.interface_af.OnDesListener
import com.project.how.interface_af.interface_ff.OnAirportListener
import com.project.how.view.dialog.bottom_sheet_dialog.AirportBottomSheetDialog
import com.project.how.view_model.BookingViewModel
import com.project.how.view_model.MemberViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class OneWaySearchFragment : Fragment(), OnAirportListener {
    private var _binding : FragmentOneWaySearchBinding? = null
    private val binding : FragmentOneWaySearchBinding
        get() = _binding!!
    private val bookingViewModel : BookingViewModel by viewModels()
    private var date : String? = null
    private var connectingFlightCheck = true
    private var destination : String? = null
    private var departure : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_one_way_search, container, false)
        binding.oneWay = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun showAirportInput(type: Int){
        val airport = AirportBottomSheetDialog(type, this)
        airport.show(childFragmentManager, "AirportBottomSheetDialog")
    }

    fun showCalendar(){
        val constraints = CalendarConstraints.Builder()
            .setStart(Calendar.getInstance().timeInMillis)
            .build()

        val calendar = MaterialDatePicker.Builder.datePicker()
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .setCalendarConstraints(constraints)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .build()
        calendar.show(childFragmentManager, "MaterialDatePicker")

        calendar.addOnPositiveButtonClickListener {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it
            val format = SimpleDateFormat("yyyy-MM-dd")
            val formatted = format.format(utc.time)
            binding.dateOutput.text = formatted
            date = formatted
        }
    }

    fun search(){

    }

    fun swap(){
        val temp = departure
        departure = destination
        destination = temp

        binding.departure.text = departure
        binding.destination.text = destination
    }

    override fun onAirportListener(type: Int, airport: String) {
        if(type == AirportBottomSheetDialog.DEPARTURE){
            departure = airport
            binding.departure.text = airport
        }else{
            destination = airport
            binding.destination.text = airport
        }
    }
}