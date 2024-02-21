package com.project.how.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.AiSchedule
import com.project.how.databinding.AiScheduleItemBinding

//현재 폐기
class AiScheduleAdapter(data : List<AiSchedule>)
    : RecyclerView.Adapter<AiScheduleAdapter.ViewHolder>() {
        private val schedule = data
        private val drawerCheck = mutableListOf<Boolean>()

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
                binding.places.text = getPlacesText(data.places)
                Glide.with(binding.root)
                    .load(data.image)
                    .error(BuildConfig.ERROR_IMAGE_URl)
                    .into(binding.image)

                setDaysTab(binding, schedule.size)

                binding.drawer.setOnClickListener {
                    onDrawerListener(binding, position)
                    drawerCheck[position] = !drawerCheck[position]
                }

                binding.daysTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        val selectedTabPosition = binding.daysTab.selectedTabPosition
                        Log.d("OnTabSelected", "selectedTabPosition : $selectedTabPosition")
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }
                })
            }
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
        }else{
            binding.drawer.setImageResource(R.drawable.arrow_up)
            binding.daysTab.visibility = View.VISIBLE
            binding.daysTitle.visibility = View.VISIBLE
            binding.create.visibility = View.VISIBLE
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
            val tab = binding.daysTab.newTab().setText("\"${i}일차\"")
            binding.daysTab.addTab(tab)
        }
    }
}