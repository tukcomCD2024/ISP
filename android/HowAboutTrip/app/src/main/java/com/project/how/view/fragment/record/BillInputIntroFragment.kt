package com.project.how.view.fragment.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.FragmentBillInputIntroBinding


class BillInputIntroFragment : Fragment() {
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
}