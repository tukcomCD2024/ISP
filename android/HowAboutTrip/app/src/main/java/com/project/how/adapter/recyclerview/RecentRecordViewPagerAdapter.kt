package com.project.how.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.recyclerview.RecentRecord
import com.project.how.databinding.ViewpagerRecentRecordItemBinding

class RecentRecordViewPagerAdapter(val context : Context, data : List<RecentRecord>)
    : RecyclerView.Adapter<RecentRecordViewPagerAdapter.ViewHolder>() {
        private var data = data
    inner class ViewHolder(val binding : ViewpagerRecentRecordItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : RecentRecord){
            binding.titleDate.text = context.getString(R.string.recent_record_text, data.date, data.title)
            if (data.firstImage == null && data.secondImage == null){
                Glide.with(binding.root)
                    .load(R.drawable.event_viewpager_test)
                    .error(BuildConfig.ERROR_IMAGE_URl)
                    .into(binding.firstImage)
                Glide.with(binding.root)
                    .load(R.drawable.event_viewpager_test)
                    .error(BuildConfig.ERROR_IMAGE_URl)
                    .into(binding.firstImage)
            }else{
                Glide.with(binding.root)
                    .load(data.firstImage)
                    .error(BuildConfig.ERROR_IMAGE_URl)
                    .into(binding.firstImage)
                Glide.with(binding.root)
                    .load(data.secondImage)
                    .error(BuildConfig.ERROR_IMAGE_URl)
                    .into(binding.firstImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(ViewpagerRecentRecordItemBinding.inflate(
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