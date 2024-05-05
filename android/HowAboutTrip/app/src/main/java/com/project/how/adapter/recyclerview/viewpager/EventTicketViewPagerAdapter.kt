package com.project.how.adapter.recyclerview.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.recyclerview.EventViewPager
import com.project.how.databinding.ViewpagerRoundTicketEventBinding

class EventTicketViewPagerAdapter(eventInfo : List<EventViewPager>)
    : RecyclerView.Adapter<EventTicketViewPagerAdapter.ViewHolder>() {
        private var data = eventInfo
    inner class ViewHolder(val binding : ViewpagerRoundTicketEventBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : EventViewPager){
            binding.text.text = data.title
            if(data.image == null){
                Glide.with(binding.root)
                    .load("https://img.freepik.com/free-photo/famous-tower-bridge-in-the-evening-london-england_268835-1390.jpg?w=1380&t=st=1711373689~exp=1711374289~hmac=48368a0b7fab5f7d212f39979735f02fd5ed184aab1ef0e8535de2e856482de6")
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ViewpagerRoundTicketEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }
}