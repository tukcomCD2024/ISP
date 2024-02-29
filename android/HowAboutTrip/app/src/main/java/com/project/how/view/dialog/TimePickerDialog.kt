package com.project.how.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.project.how.R
import com.project.how.databinding.DialogTimepickerBinding
import com.project.how.interface_af.OnTimeListener
import kotlin.time.Duration.Companion.hours

class TimePickerDialog(private val onTimeListener: OnTimeListener) : DialogFragment() {
    private var _binding: DialogTimepickerBinding? = null
    private val binding: DialogTimepickerBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_timepicker, container, false)
        binding.time = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.timePicker.setIs24HourView(true)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    fun cancel(){
        dismiss()
    }

    fun save(){
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute
        val time = getString(R.string.time_format, hour.toString(), minute.toString())
        onTimeListener.onSaveTime(time)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}