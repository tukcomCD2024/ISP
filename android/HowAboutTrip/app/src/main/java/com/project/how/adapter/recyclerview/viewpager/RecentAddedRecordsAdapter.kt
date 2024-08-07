package com.project.how.adapter.recyclerview.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.data_class.recyclerview.RecentAddedRecord
import com.project.how.databinding.ViewpagerRecentRecordItemBinding

class RecentAddedRecordsAdapter(
    data : List<RecentAddedRecord>
) : RecyclerView.Adapter<RecentAddedRecordsAdapter.ViewHolder>() {
    private val recent = data

    inner class ViewHolder(val binding : ViewpagerRecentRecordItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : RecentAddedRecord){
            binding.titleDate.text = if (data.endDate == null) "${data.startDate}\n${data.title}" else "${data.startDate}\n${data.endDate}\n${data.title}"

            if (!data.image.isNullOrBlank() && !data.countryImage.isNullOrBlank()){
                Glide.with(binding.root)
                    .load(data.image)
                    .error(BuildConfig.ERROR_IMAGE_URL)
                    .into(binding.firstImage)

                Glide.with(binding.root)
                    .load(data.countryImage)
                    .error(BuildConfig.ERROR_IMAGE_URL)
                    .into(binding.secondImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ViewpagerRecentRecordItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = recent.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = recent[position]

        holder.bind(data)
        holder.itemView.setOnClickListener {

        }
    }

    interface OnItemClickListener{
        fun onItemClickListener(id : Long)
    }
}