package com.project.how.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.schedule.AiDaysScheduleAdapter
import com.project.how.data_class.recyclerview.schedule.AiSchedule
import com.project.how.databinding.DialogAiScheduleBinding
import com.project.how.interface_af.OnAddListener
import com.project.how.view.dp.DpPxChanger
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AiScheduleDialog(private val data : AiSchedule, private val onAddListener: OnAddListener) : DialogFragment() {
    private var _binding : DialogAiScheduleBinding? = null
    private val binding : DialogAiScheduleBinding
        get() = _binding!!
    private lateinit var adapter : AiDaysScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_ai_schedule, container, false)
        binding.ai = this
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = AiDaysScheduleAdapter(requireContext(), data.dailySchedule[0])
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val params = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params
        binding.daySchedules.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            binding.title.text = data.title
            Glide.with(binding.root)
                .load(data.image)
                .error(BuildConfig.ERROR_IMAGE_URL)
                .into(binding.image)
            binding.places.text = getPlacesText(data.places)
            setDaysTab()
            setDaysTabItemMargin()
            binding.daysTitle.text = getString(R.string.days_title, (1).toString(), getDaysTitle(0))
        }

        binding.daysTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedTabPosition = binding.daysTab.selectedTabPosition
                Log.d("OnTabSelected", "selectedTabPosition : $selectedTabPosition")
                binding.daysTitle.text = getString(R.string.days_title, (selectedTabPosition + 1).toString(), getDaysTitle(selectedTabPosition))
                lifecycleScope.launch {
                    adapter.update(data.dailySchedule[selectedTabPosition])
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addCalendar(){
        onAddListener.onAddListener()
        dismiss()
    }

    private fun getPlacesText(places : List<String>): String{
        var hashTagPlaces = ""
        places.forEachIndexed { index, s ->
            if (index == places.lastIndex){
                hashTagPlaces += "#$s"
            }else{
                hashTagPlaces += "#$s "
            }
        }
        return hashTagPlaces
    }

    private fun setDaysTab(){
        for(i in 1..data.dailySchedule.size){
            val tab = binding.daysTab.newTab().setText("${i}일차")
            binding.daysTab.addTab(tab)
        }
    }

    private fun setDaysTabItemMargin(){
        val tabs = binding.daysTab.getChildAt(0) as ViewGroup
        for(i in 0 until tabs.childCount){
            val tab = tabs.getChildAt(i)
            val lp = tab.layoutParams as LinearLayout.LayoutParams
            val dpPxChanger = DpPxChanger()
            lp.marginEnd = dpPxChanger.dpToPx(requireContext(), TAB_ITEM_MARGIN)
            lp.width = dpPxChanger.dpToPx(requireContext(), TAB_ITEM_WIDTH)
            lp.height = dpPxChanger.dpToPx(requireContext(), TAB_ITEM_HEIGHT)
            tab.layoutParams = lp
        }
        binding.daysTab.requestLayout()
    }

    private fun getDaysTitle(tabNum : Int) : String{
        val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE)
        val formatter = DateTimeFormatter.ofPattern("MM.dd")
        return startDate.plusDays(tabNum.toLong()).format(formatter)
    }

    companion object{
        const val TAB_ITEM_MARGIN = 8
        const val TAB_ITEM_WIDTH = 70
        const val TAB_ITEM_HEIGHT = 24
    }
}