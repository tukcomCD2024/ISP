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
import androidx.fragment.app.viewModels
import com.project.how.R
import com.project.how.databinding.DialogYesOrNoBinding
import com.project.how.interface_af.OnYesOrNoListener
import com.project.how.view_model.MemberViewModel
import com.project.how.view_model.ScheduleViewModel

class YesOrNoDialog(
    private val target : String,
    private val functionType : Int,
    private val position : Int = 0,
    private val onYesOrNoListener: OnYesOrNoListener) : DialogFragment() {
    private var _binding : DialogYesOrNoBinding? = null
    private val binding : DialogYesOrNoBinding
        get() = _binding!!
    private lateinit var functionInfo : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        functionInfo = setFunctionInfo(functionType)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_yes_or_no, container, false)
        binding.dialog = this
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        if (target.isEmpty()){
            binding.target.text = functionInfo
            binding.functionInfo.visibility = View.GONE
        }else{
            binding.target.text = target
            binding.functionInfo.text = functionInfo
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun yes(){
        when(functionType){
            SCHEDULE_DELETE -> {
                onYesOrNoListener.onScheduleDeleteListener(position)
                dismiss()
            }
            CAMERA_CHECK -> {
                onYesOrNoListener.onCameraListener(true)
                dismiss()
            }
            KEEP_CHECK -> {}
            OCR_CHECK -> {
                onYesOrNoListener.onOcrListener(true)
                dismiss()
            }
            else -> {
                dismiss()
            }
        }
    }

    fun no(){
        when(functionType){
            SCHEDULE_DELETE -> {
                dismiss()
            }
            CAMERA_CHECK -> {
                onYesOrNoListener.onCameraListener(false)
                dismiss()
            }
            KEEP_CHECK->{}
            OCR_CHECK ->{
                onYesOrNoListener.onOcrListener(false)
                dismiss()
            }
            else -> {
                dismiss()
            }
        }
    }

    private fun setFunctionInfo(type : Int) : String{
        var result = ""
        when(type){
            SCHEDULE_DELETE -> {result = getString(R.string.schedule_delete_function_info)}
            CAMERA_CHECK -> {result = getString(R.string.camera_function_info)}
            KEEP_CHECK -> {result = getString(R.string.keep_check_function_info)}
            OCR_CHECK -> {result = getString(R.string.ocr_function_info)}
            else -> {result = getString(R.string.unknown_function_info)}
        }

        return result
    }

    companion object{
        const val SCHEDULE_DELETE = 0
        const val CAMERA_CHECK = 1
        const val KEEP_CHECK = 2
        const val OCR_CHECK = 3
    }
}