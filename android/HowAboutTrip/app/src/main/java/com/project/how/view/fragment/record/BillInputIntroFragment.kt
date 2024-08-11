package com.project.how.view.fragment.record

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.FragmentBillInputIntroBinding
import com.project.how.interface_af.OnYesOrNoListener
import com.project.how.view.dialog.YesOrNoDialog
import androidx.navigation.fragment.findNavController
import com.project.how.view.activity.record.BillInputActivity


class BillInputIntroFragment : Fragment(), OnYesOrNoListener {
    private var _binding : FragmentBillInputIntroBinding? = null
    private val binding : FragmentBillInputIntroBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bill_input_intro, container, false)
        binding.intro = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun ocr(){
        YesOrNoDialog(
            "",
            YesOrNoDialog.CAMERA_CHECK,
            onYesOrNoListener = this
        ).show(childFragmentManager, "YesOrNoDialog")
    }

    fun nonOcr() {
        findNavController().navigate(BillInputIntroFragmentDirections.actionBillInputIntroFragmentToBillNonOcrFragment())
    }

    override fun onScheduleDeleteListener(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onKeepCheckListener() {
        TODO("Not yet implemented")
    }

    override fun onCameraListener(answer: Boolean) {
        val activity = activity as? BillInputActivity
        val action = BillInputIntroFragmentDirections.actionBillInputIntroFragmentToOcrFragment(
            answer,
            activity?.getCurrentDate() ?: "알 수 없음",
            activity?.getCurrency() ?: "원",
            activity?.getId() ?: -1L
        )
        findNavController().navigate(action)
    }

}