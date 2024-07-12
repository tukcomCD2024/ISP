package com.project.how.adapter.recyclerview.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.R
import com.project.how.data_class.dto.weather.GetWeeklyWeathersResponseElement
import com.project.how.databinding.WeekWeatherItemBinding

class WeekWeatherAdapter(private val data : List<GetWeeklyWeathersResponseElement>)
    : RecyclerView.Adapter<WeekWeatherAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: WeekWeatherItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bind(data: GetWeeklyWeathersResponseElement){
                binding.week.text = data.date
                binding.temp.text = data.temp
                Glide.with(binding.root)
                    .load(data.iconUrl)
                    .error(R.drawable.icon_question)
                    .into(binding.image)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        WeekWeatherItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemViewType(position: Int): Int = position
    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

}