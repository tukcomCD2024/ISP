package com.project.how.adapter.recyclerview.booking.hotel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.data_class.recyclerview.RecentHotel
import com.project.how.databinding.RecentHotelItemBinding

class RecentHotelAdapter(recentHotel : List<RecentHotel>, private val onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<RecentHotelAdapter.ViewHolder>() {
        private var data = recentHotel
    inner class ViewHolder(val binding : RecentHotelItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : RecentHotel){
            binding.name.text = data.name
            binding.address.text = data.address
            binding.date.text = data.date
            if(data.image == null){
                Glide.with(binding.root)
                    .load("https://www.hankyu-hotel.com/-/media/hotel/res/osaka/accommodation/images/02_Standard%20Twin_1.jpg")
                    .error(BuildConfig.ERROR_IMAGE_URL)
                    .into(binding.image)
            }else{
                Glide.with(binding.root)
                    .load(data.image)
                    .error(BuildConfig.ERROR_IMAGE_URL)
                    .into(binding.image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        RecentHotelItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClickListener(data.id)
        }
    }

    interface OnItemClickListener{
        fun onItemClickListener(id : Long)
    }
}