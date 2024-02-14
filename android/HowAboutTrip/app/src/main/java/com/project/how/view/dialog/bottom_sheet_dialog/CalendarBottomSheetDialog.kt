package com.project.how.view.dialog.bottom_sheet_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.R
import com.project.how.adapter.CalendarAdapter
import com.project.how.databinding.CalendarBottomSheetBinding
import com.project.how.interface_af.OnDateTimeListener
import com.project.how.interface_af.OnTimeListener
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.TimePickerDialog
import com.project.how.view_model.CalendarViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarBottomSheetDialog(private val type : Int, private val onDateTimeListener: OnDateTimeListener) :
    BottomSheetDialogFragment(), CalendarAdapter.OnItemClickListener, OnTimeListener {
    private var _binding: CalendarBottomSheetBinding? = null
    private val binding : CalendarBottomSheetBinding
        get() = _binding!!
    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var days : List<Int>
    private lateinit var adapter : CalendarAdapter
    private var selectedDay : LocalDate? = null
    private var time : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.calendar_bottom_sheet, container, false)
        binding.calendar = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(type){
            BASIC ->{
                binding.timeOutput.visibility = View.GONE
                binding.timeInput.visibility = View.GONE
            }
            DEPARTURE ->{
                binding.timeOutput.visibility = View.GONE
                binding.timeInput.visibility = View.GONE
                binding.timeOutput.text = resources.getString(R.string.departure_time, resources.getString(R.string.not_entered))
            }
            ENTRANCE ->{
                binding.timeOutput.visibility = View.GONE
                binding.timeInput.visibility = View.GONE
                binding.timeOutput.text = resources.getString(R.string.entrance_time, resources.getString(R.string.not_entered))
            }
        }
        setCalenderBottomSheetView()
    }

    fun setCalenderBottomSheetView() {
        initializeCalendarBottomSheet()
        observeCalendarData()
    }

    private fun initializeCalendarBottomSheet() {
        viewModel.selectedDate.observe(viewLifecycleOwner) { sd ->
            Log.d("selectedData observe", "initializeCalendarBottom() 시작")
            days = listOf()
            adapter = CalendarAdapter(days, sd, this)
            binding.days.adapter = adapter
        }
    }

    private fun observeCalendarData() {
        lifecycleScope.launch {
            viewModel.init().collect { updatedDays ->
                viewModel.selectedDate.observe(viewLifecycleOwner) { sd ->
                    Log.d("selectedData observe", "observeCalendarData() 시작")
                    adapter.update(updatedDays, sd)
                    binding.month.text = sd.month.value.toString()
                    binding.year.text = sd.year.toString()
                }
            }
        }
    }

    fun minusMonth(){
        lifecycleScope.launch {
            viewModel.minusSelectedDate().collect { updatedDays ->
                viewModel.selectedDate.observe(viewLifecycleOwner) { sd ->
                    Log.d("selectedData observe", "left.setOnClickListener() 시작")
                    adapter.update(updatedDays, sd)
                }
            }
        }
    }

    fun plusMonth(){
        lifecycleScope.launch {
            viewModel.plusSelectedDate().collect { updatedDays ->
                viewModel.selectedDate.observe(viewLifecycleOwner) { sd ->
                    Log.d("selectedData observe", "right.setOnClickListener() 시작")
                    adapter.update(updatedDays, sd)
                }
            }
        }
    }

    fun resetDay() {
        adapter.resetSelected()
        selectedDay = null
    }

    fun showTimePicker(){
        val timepicker = TimePickerDialog(this)
        timepicker.show(childFragmentManager, "TimePickerDialog")
    }

    fun confirm(){
        if (selectedDay == null){
            val message = mutableListOf<String>()
            message.add(resources.getString(R.string.date))
            val confirm = ConfirmDialog(message)
            confirm.show(childFragmentManager, "ConfirmDialog")
        }else{
            val date = selectedDay!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            onDateTimeListener.onSaveDate(date, type)
            dismiss()
        }
    }

    override fun onItemClickListener(selected: MutableList<Boolean>, sd : LocalDate, position: Int) {
        adapter.resetSelected()
        selected[position] = true
        selectedDay = sd
    }

    override fun onSaveTime(time: String) {
        when(type){
            DEPARTURE ->{
                binding.timeOutput.text = resources.getString(R.string.departure_time, time)
                this.time = time
            }
            ENTRANCE ->{
                binding.timeOutput.text = resources.getString(R.string.entrance_time, time)
                this.time = time
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object{
        const val BASIC = 0
        const val DEPARTURE = 1
        const val ENTRANCE = 2
    }


}