package com.project.how.view.bottom_sheet_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.R
import com.project.how.adapter.CalendarAdapter
import com.project.how.data_class.dto.AuthRecreateRequest
import com.project.how.databinding.CalendarBottomSheetBinding
import com.project.how.view_model.CalendarViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarBottomSheetDialog : BottomSheetDialogFragment(), CalendarAdapter.OnItemClickListener {
    private lateinit var binding: CalendarBottomSheetBinding
    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var days : List<Int>
    private lateinit var adapter : CalendarAdapter
    private var selectedDay : LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.calendar_bottom_sheet, container, false)
        binding.calendar = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            binding.days.layoutManager = GridLayoutManager(context, 7)
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


    fun resetDay() {
        selectedDay = null
        adapter.resetSelected()
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

    fun confirm(){
        
    }

    override fun onItemClickListener(data: Int, selected: MutableList<Boolean>, position: Int, sd : LocalDate) {
        adapter.resetSelected()
        selected[position] = !selected[position]
        selectedDay = sd
    }

}