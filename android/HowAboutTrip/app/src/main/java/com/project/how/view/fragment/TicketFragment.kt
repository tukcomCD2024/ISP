package com.project.how.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.FragmentTicketBinding

class TicketFragment : Fragment() {
    private var _binding : FragmentTicketBinding? = null
    private val binding : FragmentTicketBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ticket, container, false)
        binding.ticket = this
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}