package com.project.how.view.dialog.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.R
import com.project.how.adapter.recyclerview.RadioButtonAdapter
import com.project.how.databinding.PurposeBottomSheetBinding
import com.project.how.interface_af.OnPurposeListener
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class PurposeBottomSheetDialog(private val onPurposeListener : OnPurposeListener)
    : BottomSheetDialogFragment(), RadioButtonAdapter.OnItemClickListener  {
    private var _binding : PurposeBottomSheetBinding? = null
    private val binding : PurposeBottomSheetBinding
        get() = _binding!!
    private val whoList = mutableListOf<String>()
    private val activityLevelList = mutableListOf<String>()
    private val purposeList = mutableListOf<String>()
    private val transportationList = mutableListOf<String>()
    private lateinit var whoAdapter : RadioButtonAdapter
    private lateinit var activityLevelAdapter : RadioButtonAdapter
    private lateinit var purposeAdapter : RadioButtonAdapter
    private lateinit var transportationAdapter: RadioButtonAdapter
    private var who : String? = null
    private var activityLevel : String? = null
    private var theme : MutableList<String> = mutableListOf()
    private var transportation : MutableList<String> = mutableListOf()
    private var check = mutableListOf<Boolean>(false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            whoList.add(resources.getString(R.string.single))
            whoList.add(resources.getString(R.string.couple))
            whoList.add(resources.getString(R.string.friend))
            whoList.add(resources.getString(R.string.family))

            whoAdapter = RadioButtonAdapter(whoList, false, this@PurposeBottomSheetDialog, RadioButtonAdapter.WHO)
        }

        lifecycleScope.launch {
            activityLevelList.add(resources.getString(R.string.leisurely))
            activityLevelList.add(resources.getString(R.string.relaxed))
            activityLevelList.add(resources.getString(R.string.busy))
            activityLevelList.add(resources.getString(R.string.without_a_break))

            activityLevelAdapter = RadioButtonAdapter(activityLevelList, false, this@PurposeBottomSheetDialog, RadioButtonAdapter.ACTIVITY_LEVEL)

            transportationList.addAll(mutableListOf<String>(getString(R.string.walk),
                getString(R.string.bus), getString(R.string.subway),
                getString(R.string.train), getString(R.string.yacht),
                getString(R.string.boat), getString(R.string.airplane_text)))

            transportationAdapter = RadioButtonAdapter(transportationList, true, this@PurposeBottomSheetDialog, RadioButtonAdapter.TRANSPORTATION)
        }

        lifecycleScope.launch {

            purposeList.addAll(mutableListOf<String>(getString(R.string.vacation),
                getString(R.string.holiday),
                getString(R.string.business_trip),
                getString(R.string.honeymoon),
                getString(R.string.sightseeing),
                getString(R.string.shopping),
                getString(R.string.healing),
                getString(R.string.activities),
                getString(R.string.couple_date),
                getString(R.string.filial_piety_trip), getString(R.string.graduation_trip),
                getString(
                    R.string.pilgrimage
                )))

            purposeAdapter = RadioButtonAdapter(purposeList, true, this@PurposeBottomSheetDialog, RadioButtonAdapter.THEME)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.purpose_bottom_sheet, container, false)
        binding.purpose = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.whoList.adapter = whoAdapter
        binding.activityLevelList.adapter = activityLevelAdapter
        binding.purposeList.adapter = purposeAdapter
        binding.transportationList.adapter = transportationAdapter
        return binding.root
    }

    fun reset(){
        who = null
        activityLevel = null
        theme.clear()

        lifecycleScope.launch {
            whoAdapter.reset()
            whoAdapter.notifyDataSetChanged()
            activityLevelAdapter.reset()
            activityLevelAdapter.notifyDataSetChanged()
            purposeAdapter.reset()
            purposeAdapter.notifyDataSetChanged()
        }
    }

    fun confirm(){
        lifecycleScope.launch {
            val themeJob = lifecycleScope.launch {
                getPurposeTheme()
                check[2] = theme.isNotEmpty()
                getTransportation()
                check[3] = transportation.isNotEmpty()
            }
            val whoAndActivityJob = lifecycleScope.launch {
                check[0] = who != null
                check[1] = activityLevel != null
            }

            listOf(themeJob, whoAndActivityJob).joinAll()

            if (check[0])
                onPurposeListener.onWhoListener(who!!)
            if (check[1])
                onPurposeListener.onActivityLevelListener(activityLevel!!)
            if (check[2])
                onPurposeListener.onPurposeListener(theme)
            if (check[3])
                onPurposeListener.onTransportationListener(transportation)

            dismiss()
        }
    }

    private fun getPurposeTheme(){
        val t = purposeAdapter.getDatas()
        theme.addAll(t)
    }

    private fun getTransportation(){
        val t = transportationAdapter.getDatas()
        transportation.addAll(t)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickListener(data: String, type: Int, position: Int) {
        if (type == RadioButtonAdapter.WHO){
            who = data
        }else if(type == RadioButtonAdapter.ACTIVITY_LEVEL){
            activityLevel = data
        }
    }
}