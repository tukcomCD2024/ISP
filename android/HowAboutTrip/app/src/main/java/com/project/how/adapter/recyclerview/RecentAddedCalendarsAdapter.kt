package com.project.how.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.data_class.dto.GetLatestSchedulesResponse
import com.project.how.data_class.dto.GetLatestSchedulesResponseElement
import com.project.how.databinding.RecentAddedCalendarsItemBinding

class RecentAddedCalendarsAdapter(data : GetLatestSchedulesResponse, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<RecentAddedCalendarsAdapter.ViewHolder>() {
    private var recentAddedCalendars = data

    inner class ViewHolder(val binding : RecentAddedCalendarsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : GetLatestSchedulesResponseElement){
            binding.name.text = data.scheduleName
            binding.des.text = data.city
            binding.places.text = getPlacesString(data.plan)
            Glide.with(binding.root)
                .load(data.imageUrl)
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
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClickListener(data.id)
        }
    }

    override fun getItemViewType(position: Int): Int = position

    private fun getPlacesString(places : List<String>) : String{
        var result = ""
        places.forEach {
            result += "#$it "
        }
        return result
    }

    interface OnItemClickListener{
        fun onItemClickListener(id : Long)
    }
}