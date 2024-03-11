package com.project.how.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.EventViewPager
import com.project.how.databinding.ViewpagerRoundEventBinding

class EventViewPagerAdapter(
    eventInfo : List<EventViewPager>)
    : RecyclerView.Adapter<EventViewPagerAdapter.ViewHolder>() {
        private var datas = eventInfo
        inner class ViewHolder(val binding : ViewpagerRoundEventBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(data : EventViewPager){
                binding.text.text = data.title
                if(data.image == null){
                    Glide.with(binding.root)
                        .load(R.drawable.event_viewpager_test)
                        .error(BuildConfig.ERROR_IMAGE_URl)
                        .into(binding.image)
                }else{
                    Glide.with(binding.root)
                        .load(data.image)
                        .error(BuildConfig.ERROR_IMAGE_URl)
                        .into(binding.image)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ViewpagerRoundEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    fun update(newDatas : List<EventViewPager>){
        datas = newDatas
        notifyDataSetChanged()
    }
}