package com.project.how.adapter.recyclerview.record

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.databinding.BillListItemBinding

class BillListAdapter (

) : RecyclerView.Adapter<BillListAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: BillListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : ){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}