package com.project.how.adapter.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.data_class.dto.GetFlightOffersResponseElement
import com.project.how.databinding.AirplaneListItemBinding

class AirplaneListAdapter(private val data : ArrayList<GetFlightOffersResponseElement>) : RecyclerView.Adapter<AirplaneListAdapter.ViewHolder>(){
    inner class ViewHolder(val binding : AirplaneListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : GetFlightOffersResponseElement, position: Int){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        AirplaneListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}