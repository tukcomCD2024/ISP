package com.project.how.adapter.recyclerview.viewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.data_class.recyclerview.schedule.EventViewPager
import com.project.how.databinding.ViewpagerRoundEventBinding

class EventViewPagerAdapter(
    eventInfo : List<EventViewPager>)
    : RecyclerView.Adapter<EventViewPagerAdapter.ViewHolder>() {
        private var datas = eventInfo
        inner class ViewHolder(val binding : ViewpagerRoundEventBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(data : EventViewPager){
                binding.text.text = data.title
                if (data.fade)
                    binding.fade.visibility = View.VISIBLE
                else
                    binding.fade.visibility = View.INVISIBLE
                if(data.image == null){
                    Glide.with(binding.root)
                        .load("https://img.freepik.com/free-photo/asian-woman-wearing-japanese-traditional-kimono-at-yasaka-pagoda-and-sannen-zaka-street-in-kyoto-japan_335224-121.jpg?w=1380&t=st=1711376316~exp=1711376916~hmac=c66b5094e4df32daf35dbf1047664b2c5317cbb3215b8c6615900aa0f80fbf6b")
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