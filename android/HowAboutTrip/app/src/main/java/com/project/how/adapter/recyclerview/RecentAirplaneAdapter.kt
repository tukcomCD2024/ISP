package com.project.how.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.roomdb.RecentAirplane
import com.project.how.databinding.RecentAirplaneItemBinding

class RecentAirplaneAdapter(recentAirplane: List<RecentAirplane>, private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecentAirplaneAdapter.ViewHolder>(){
        private val data = recentAirplane
    inner class ViewHolder(val binding : RecentAirplaneItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : RecentAirplane){
            binding.des.text = data.name
            binding.time1.text = data.time1
            binding.time2.text = data.time2?: ""
            if(data.image == null){
                Glide.with(binding.root)
                    .load(R.drawable.event_image_airplnae_temp)
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
        holder.itemView.setOnClickListener {
            if (data.skyscannerUrl != null){
                onItemClickListener.onItemClickListener(data.skyscannerUrl)
            }
        }
    }

    interface  OnItemClickListener {
        fun onItemClickListener(url : String)
    }
}