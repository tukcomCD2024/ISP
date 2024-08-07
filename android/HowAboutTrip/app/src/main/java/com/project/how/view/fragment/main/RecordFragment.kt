package com.project.how.view.fragment.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.project.how.R
import com.project.how.adapter.recyclerview.viewpager.RecentAddedRecordsAdapter
import com.project.how.data_class.recyclerview.record.RecentAddedRecord
import com.project.how.databinding.FragmentRecordBinding
import com.project.how.view.activity.record.BillListActivity
import com.project.how.view.activity.record.LocationMapActivity
import com.project.how.view_model.MemberViewModel
import kotlinx.coroutines.launch

class RecordFragment : Fragment() {
    private var _binding : FragmentRecordBinding? = null
    private val binding : FragmentRecordBinding
        get() = _binding!!
    private lateinit var adapter : RecentAddedRecordsAdapter
    private lateinit var recent : List<RecentAddedRecord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            recent = listOf(
                RecentAddedRecord(0, "2024-06-01", "2024-06-04", null, null,"대만 가족여행"),
                RecentAddedRecord(0, "2024-04-17", null, "https://media.triple.guide/triple-cms/c_limit,f_auto,h_1024,w_1024/eabb2441-4a98-48f4-8b86-4984823749fd.jpeg", "https://www.ana.co.jp/japan-travel-planner/okinawa/img/hero.jpg","오키나와 당일치기"),
                RecentAddedRecord(0, "2023-07-10", "2023-07-17", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQx-E0xIw05LNRipiI8aSdAIpgEWwN1bB9eZw&s", "https://i.namu.wiki/i/vMxhrnGtssBmIKfGJvHvaXbHc6i247nj-HA7aQOGpweTeENStf-kh7kz9RgwMSjics0H6iQkG-Lj-rjP_dYAXQ.webp","여름방학 독일 배낭 여행"),
            )

            adapter = RecentAddedRecordsAdapter(recent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)
        binding.picture = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recentRecord.adapter = adapter
        binding.recordCount.text = getString(R.string.record_count, 0.toString())
        binding.countTitle.text = getString(R.string.record_count_user_title, MemberViewModel.memberInfoLiveData.value?.name ?: getString(R.string.error))
        TabLayoutMediator(binding.indicator, binding.recentRecord) { _, _ -> }.attach()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun moveBillList(){
        startActivity(Intent(requireContext(), BillListActivity::class.java))
    }

    fun moveLocationMap(){
        startActivity(Intent(requireContext(), LocationMapActivity::class.java))
    }
}