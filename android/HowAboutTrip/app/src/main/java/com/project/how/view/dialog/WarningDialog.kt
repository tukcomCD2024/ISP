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
import com.project.how.databinding.DialogWarningBinding
import com.project.how.interface_af.OnDialogListener

class WarningDialog(private val message : String, private val onDialogListener : OnDialogListener): DialogFragment() {
    private var _binding : DialogWarningBinding? = null
    private val binding : DialogWarningBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_warning, container, false)
        binding.warning = this
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding.ms.text = message
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun confirm(){
        onDialogListener.onDismissListener()
        dismiss()
    }
}