package com.project.how.adapter.recyclerview.record

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.data_class.recyclerview.record.Bill
import com.project.how.databinding.BillListItemBinding

class BillListAdapter (
    data : List<Bill>,
    private val context : Context,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BillListAdapter.ViewHolder>(){
    private var bills = data.toMutableList()
    inner class ViewHolder(val binding: BillListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Bill, position: Int){
            binding.title.text = data.title
            binding.date.text = data.date
            binding.cost.text = context.getString(R.string.total_cost) + "${data.cost}"
            binding.count.text = context.getString(R.string.bill_count, data.count.toString())
            Glide.with(binding.root)
                .load(data.image ?: BuildConfig.TEMPORARY_IMAGE_URL)
                .error(BuildConfig.ERROR_IMAGE_URL)
                .into(binding.image)

            binding.delete.setOnClickListener {
                onItemClickListener.onDeleteButtonClickListener(data.id, position)
            }
            itemView.setOnClickListener {
                onItemClickListener.onItemClickListener(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        BillListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = bills.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = bills[position]
        holder.bind(data, position)
    }

    fun update(newData : List<Bill>){
        bills = newData.toMutableList()
        notifyDataSetChanged()
    }

    fun delete(position: Int){
        bills.removeAt(position)
        notifyItemRangeChanged(position, bills.lastIndex)
    }

    fun add(newData : Bill){
        bills.add(newData)
        notifyItemInserted(bills.lastIndex)
    }

    interface OnItemClickListener{
        fun onItemClickListener(id: Long)
        fun onDeleteButtonClickListener(id: Long, position: Int)
    }
}