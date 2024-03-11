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

class PurposeBottomSheetDialog(private val onPurposeListener : OnPurposeListener) : BottomSheetDialogFragment(), RadioButtonAdapter.OnItemClickListener  {
    private var _binding: PurposeBottomSheetBinding? = null
    private val binding: PurposeBottomSheetBinding
        get() = _binding!!
    private val whoList = mutableListOf<String>()
    private val activityLevelList = mutableListOf<String>()
    private val themeList = mutableListOf<String>()
    private lateinit var whoAdapter : RadioButtonAdapter
    private lateinit var activityLevelAdapter : RadioButtonAdapter
    private lateinit var themeAdapter : RadioButtonAdapter
    private var who : String? = null
    private var activityLevel : String? = null
    private var theme : MutableList<String> = mutableListOf()
    private var check = mutableListOf<Boolean>(false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            whoList.add(resources.getString(R.string.single))
            whoList.add(resources.getString(R.string.family))
            whoList.add(resources.getString(R.string.couple))
            whoList.add(resources.getString(R.string.friend))

            whoAdapter = RadioButtonAdapter(whoList, false, this@PurposeBottomSheetDialog, RadioButtonAdapter.WHO)
        }

        lifecycleScope.launch {
            activityLevelList.add(resources.getString(R.string.leisurely))
            activityLevelList.add(resources.getString(R.string.relaxed))
            activityLevelList.add(resources.getString(R.string.busy))
            activityLevelList.add(resources.getString(R.string.without_a_break))

            activityLevelAdapter = RadioButtonAdapter(activityLevelList, false, this@PurposeBottomSheetDialog, RadioButtonAdapter.ACTIVITY_LEVEL)
        }

        lifecycleScope.launch {
            themeList.add(resources.getString(R.string.atmospheric))
            themeList.add(resources.getString(R.string.renowned))
            themeList.add(resources.getString(R.string.seasonal))
            themeList.add(resources.getString(R.string.vibrant))
            themeList.add(resources.getString(R.string.historic))
            themeList.add(resources.getString(R.string.artistic))
            themeList.add(resources.getString(R.string.natural))
            themeList.add(resources.getString(R.string.adventurous))
            themeList.add(resources.getString(R.string.romantic))
            themeList.add(resources.getString(R.string.stress_relieving))
            themeList.add(resources.getString(R.string.cultural))
            themeList.add(resources.getString(R.string.enjoying_local_cuisine))

            themeAdapter = RadioButtonAdapter(themeList, true, this@PurposeBottomSheetDialog, RadioButtonAdapter.THEME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.purpose_bottom_sheet, container, false)
        binding.purpose = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.whoList.adapter = whoAdapter
        binding.activityLevelList.adapter = activityLevelAdapter
        binding.themeList.adapter = themeAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            themeAdapter.reset()
            themeAdapter.notifyDataSetChanged()
        }
    }

    fun confirm(){
        lifecycleScope.launch {
            val themeJob = lifecycleScope.launch {
                getPurposeTheme()
                check[2] = theme.isNotEmpty()
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
                onPurposeListener.onThemeListener(theme)

            dismiss()
        }
    }

    private fun getPurposeTheme(){
        val t = themeAdapter.getDatas()
        theme.addAll(t)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClickListener(data: String, type: Int) {
        if (type == RadioButtonAdapter.WHO){
            who = data
        }else if(type == RadioButtonAdapter.ACTIVITY_LEVEL){
            activityLevel = data
        }
    }
}