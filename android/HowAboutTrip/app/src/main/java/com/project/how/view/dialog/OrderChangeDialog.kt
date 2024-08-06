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
import com.project.how.adapter.recyclerview.RadioButtonAdapter
import com.project.how.data_class.recyclerview.schedule.DaysSchedule
import com.project.how.databinding.DialogOrderChangeBinding
import com.project.how.interface_af.OnOrderChangeListener

class OrderChangeDialog(private val data : MutableList<DaysSchedule>, private val currentPosition : Int, private val onOrderChangeListener: OnOrderChangeListener) : DialogFragment(), RadioButtonAdapter.OnItemClickListener {
    private var _binding : DialogOrderChangeBinding? = null
    private val binding : DialogOrderChangeBinding
        get() = _binding!!
    private lateinit var adapter : RadioButtonAdapter
    private lateinit var days : MutableList<String>
    private var position = currentPosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        days = mutableListOf()
        for (i in 1..data.size){
            days.add("${i}번째 : ${data[i-1].todo}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_order_change, container, false)
        binding.order = this
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        adapter = RadioButtonAdapter(days, false, this, RadioButtonAdapter.ORDER)
        adapter.setCheck(currentPosition)
        binding.scheduleOrder.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun cancel(){
        dismiss()
    }

    fun save(){
        onOrderChangeListener.onOrderChangeListener(position, currentPosition)
        dismiss()
    }

    override fun onItemClickListener(data: String, type: Int, position: Int) {
        this.position = position
    }
}