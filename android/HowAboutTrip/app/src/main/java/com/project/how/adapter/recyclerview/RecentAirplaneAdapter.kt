package com.project.how.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.recyclerview.RecentAirplane
import com.project.how.databinding.RecentAirplaneItemBinding

class RecentAirplaneAdapter(recentAirplane: List<RecentAirplane>)
    : RecyclerView.Adapter<RecentAirplaneAdapter.ViewHolder>(){
        private val data = recentAirplane
    inner class ViewHolder(val binding : RecentAirplaneItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : RecentAirplane){
            binding.des.text = data.des
            binding.date.text = data.date
            binding.time.text = data.time
            if(data.image == null){
                Glide.with(binding.root)
                    .load("https://mblogthumb-phinf.pstatic.net/20160614_300/ppanppane_1465870299257eqV77_PNG/%B4%EB%C7%D1%C7%D7%B0%F8_%B7%CE%B0%ED_%282%29.png?type=w800")
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
        RecentAirplaneItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = data.size
    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }
}