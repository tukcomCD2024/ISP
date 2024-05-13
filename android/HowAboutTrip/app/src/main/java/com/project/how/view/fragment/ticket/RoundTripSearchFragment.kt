package com.project.how.view.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.project.how.R
import com.project.how.databinding.FragmentRoundTripSearchBinding

class RoundTripSearchFragment : Fragment() {
    private var _binding : FragmentRoundTripSearchBinding? = null
    private val binding : FragmentRoundTripSearchBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_round_trip_search, container, false)
        binding.roundTrip = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}