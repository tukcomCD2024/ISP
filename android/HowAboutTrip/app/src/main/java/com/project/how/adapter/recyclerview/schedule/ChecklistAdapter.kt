package com.project.how.adapter.recyclerview.schedule

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.databinding.ChecklistItemBinding

class ChecklistAdapter : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ChecklistItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(){

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