package com.project.how.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.data_class.RecentAddedCalendar
import com.project.how.databinding.RecentAddedCalendarsItemBinding

class RecentAddedCalendarsAdapter(data : List<RecentAddedCalendar>) : RecyclerView.Adapter<RecentAddedCalendarsAdapter.ViewHolder>() {
    private var recentAddedCalendars = data

    inner class ViewHolder(val binding : RecentAddedCalendarsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : RecentAddedCalendar){
            binding.des.text = data.des
            binding.places.text = getPlacesString(data.places)
            Glide.with(binding.root)
                .load(data.image)
                .error(BuildConfig.ERROR_IMAGE_URl)
                .into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RecentAddedCalendarsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = recentAddedCalendars.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = recentAddedCalendars[position]
        holder.bind(data)
    }

    override fun getItemViewType(position: Int): Int = position

    private fun getPlacesString(places : MutableList<String>) : String{
        var result = ""
        places.forEach {
            result += "#$it "
        }
        return result
    }
}