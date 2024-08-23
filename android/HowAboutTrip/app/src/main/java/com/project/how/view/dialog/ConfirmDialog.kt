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
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.databinding.DialogConfirmBinding

class ConfirmDialog(private val notEntered : List<String>) : DialogFragment() {
    private var _binding: DialogConfirmBinding? = null
    private val binding: DialogConfirmBinding
        get() = _binding!!
    private var notEnteredMessage =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeMessage()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_confirm, container, false)
        binding.confirm = this
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding.notEnteredList.text = notEnteredMessage
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun makeMessage(){
        notEntered.forEachIndexed { index, s ->
            if (index == notEntered.lastIndex){
                notEnteredMessage += s
            }else{
                notEnteredMessage += "$s, "
            }
        }
    }
}