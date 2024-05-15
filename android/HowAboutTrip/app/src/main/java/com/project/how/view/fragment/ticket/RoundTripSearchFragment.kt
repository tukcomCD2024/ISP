package com.project.how.view.fragment.ticket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.project.how.R
import com.project.how.data_class.dto.GetFlightOffersRequest
import com.project.how.databinding.FragmentRoundTripSearchBinding
import com.project.how.interface_af.interface_ff.OnAirportListener
import com.project.how.view.activity.ticket.AirplaneListActivity
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.bottom_sheet_dialog.AirportBottomSheetDialog
import com.project.how.view_model.BookingViewModel
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class RoundTripSearchFragment : Fragment(), OnAirportListener {
    private var _binding : FragmentRoundTripSearchBinding? = null
    private val binding : FragmentRoundTripSearchBinding
        get() = _binding!!
    private val bookingViewModel : BookingViewModel by viewModels()
    private var departureDate : String? = null
    private var desnationDate : String? = null
    private var connectingFlightCheck = true
    private var destination : String? = null
    private var departure : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_round_trip_search, container, false)
        binding.roundTrip = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookingViewModel.flightOffersLiveData.observe(viewLifecycleOwner){

        }
    }

    fun showAirportInput(type: Int){
        val airport = AirportBottomSheetDialog(type, this)
        airport.show(childFragmentManager, "AirportBottomSheetDialog")
    }

    fun showCalendar(){
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
            binding.dateOutput.text = getString(R.string.date_text, formatted, formattedSecond)
            departureDate = formatted
        }
    }

    fun search(){
        lifecycleScope.launch {
            if ((departure != null) || (destination != null) || (departureDate != null)){
                val adults = binding.adultNumber.text.toString().toLong()
                val children = binding.childNumber.text.toString().toLong()
                val input = GetFlightOffersRequest(
                    departure!!,
                    destination!!,
                    departureDate!!,
                    desnationDate!!,
                    adults,
                    children,
                    50,
                    connectingFlightCheck
                )
                bookingViewModel.getFlightOffers(requireContext(), MemberViewModel.tokensLiveData.value!!.accessToken, input).collect{check->
                    if (!check){
                        Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                val message = mutableListOf<String>()
                if (departure == null)
                    message.add(getString(R.string.departure))
                if (destination == null)
                    message.add(getString(R.string.destination))
                if (departureDate == null)
                    message.add(getString(R.string.date))
                val confirm = ConfirmDialog(message)
                confirm.show(childFragmentManager, "ConfirmDialog")
            }
        }
    }

    fun swap(){
        val temp = departure
        departure = destination
        destination = temp
        binding.departure.text = departure
        binding.destination.text = destination
    }

    private fun moveAirportList(){
        val intent = Intent(activity, AirplaneListActivity::class.java)

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