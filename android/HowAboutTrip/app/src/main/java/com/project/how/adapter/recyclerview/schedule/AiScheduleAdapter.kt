package com.project.how.adapter.recyclerview.schedule

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.recyclerview.schedule.AiSchedule
import com.project.how.databinding.AiScheduleItemBinding
import com.project.how.view.dialog.AiScheduleDialog
import com.project.how.view.dp.DpPxChanger
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AiScheduleAdapter(private val context: Context, data : List<AiSchedule>, private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<AiScheduleAdapter.ViewHolder>() {
        private val schedule = data
        private val drawerCheck = mutableListOf<Boolean>()
        private var selectedDay = 0

    init {
        if(drawerCheck.isEmpty()){
            for (i in schedule.indices){
                drawerCheck.add(false)
            }
        }
    }

        inner class ViewHolder(val binding : AiScheduleItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(data : AiSchedule, position: Int){
                binding.title.text = data.title
                binding.budget.text = context.getString(R.string.total_budget, data.budget.toString(), data.currency)
                binding.places.text = getPlacesText(data.places)
                Glide.with(binding.root)
                    .load(data.image)
                    .error(BuildConfig.ERROR_IMAGE_URL)
                    .into(binding.image)

                setDaysTab(binding, data.dailySchedule.size)
                setDaysTabItemMargin(context, binding)
                Log.d("AiScheduleAdapter", "data.dailySchedule.size : ${data.dailySchedule.size}")

                binding.daysTitle.text = context.getString(R.string.days_title, (1).toString(), getDaysTitle(data, 0))

                val adapter = AiDaysScheduleAdapter(context, data.dailySchedule[selectedDay], data.currency)
                binding.daySchedules.adapter = adapter

                binding.drawer.setOnClickListener {
                    onDrawerListener(binding, position)
                    drawerCheck[position] = !drawerCheck[position]
                }

                binding.create.setOnClickListener{
                    onItemClickListener.onCreateButtonClicker(data)
                }

                binding.daysTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        val selectedTabPosition = binding.daysTab.selectedTabPosition
                        Log.d("AiScheduleAdapter OnTabSelected", "selectedTabPosition : $selectedTabPosition")
                        binding.daysTitle.text = context.getString(R.string.days_title, (selectedTabPosition + 1).toString(), getDaysTitle(data, selectedTabPosition))
                        adapter.update(data.dailySchedule[selectedTabPosition])
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
            }
        }

    private fun getDaysTitle(data : AiSchedule, tabNum: Int): Any? {
        val startDate = LocalDate.parse(data.startDate, DateTimeFormatter.ISO_DATE)
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        return startDate.plusDays(tabNum.toLong()).format(formatter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        AiScheduleItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = schedule.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = schedule[position]
        holder.bind(data, position)
    }

    private fun onDrawerListener(binding: AiScheduleItemBinding, position: Int){
        if(drawerCheck[position]){
            binding.drawer.setImageResource(R.drawable.arrow_down)
            binding.daysTab.visibility = View.GONE
            binding.daysTitle.visibility = View.GONE
            binding.create.visibility = View.GONE
            binding.daySchedules.visibility = View.GONE
        }else{
            binding.drawer.setImageResource(R.drawable.arrow_up)
            binding.daysTab.visibility = View.VISIBLE
            binding.daysTitle.visibility = View.VISIBLE
            binding.create.visibility = View.VISIBLE
            binding.daySchedules.visibility = View.VISIBLE
        }
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

    private fun setDaysTab(binding : AiScheduleItemBinding, size : Int){
        for(i in 1 .. size){
            val tab = binding.daysTab.newTab().setText("${i}일차")
            binding.daysTab.addTab(tab)
        }
    }

    private fun setDaysTabItemMargin(context: Context, binding: AiScheduleItemBinding){
        val tabs = binding.daysTab.getChildAt(0) as ViewGroup
        for(i in 0 until tabs.childCount){
            val tab = tabs.getChildAt(i)
            val lp = tab.layoutParams as LinearLayout.LayoutParams
            val dpPxChanger = DpPxChanger()
            lp.marginEnd = dpPxChanger.dpToPx(context, AiScheduleDialog.TAB_ITEM_MARGIN)
            lp.width = dpPxChanger.dpToPx(context, AiScheduleDialog.TAB_ITEM_WIDTH)
            lp.height = dpPxChanger.dpToPx(context, AiScheduleDialog.TAB_ITEM_HEIGHT)
            tab.layoutParams = lp
        }
        binding.daysTab.requestLayout()
    }

    interface OnItemClickListener{
        fun onCreateButtonClicker(data : AiSchedule)
    }
}