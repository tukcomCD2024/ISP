package com.project.how.adapter.recyclerview.record

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.R
import com.project.how.data_class.dto.recode.receipt.ReceiptListItem
import com.project.how.databinding.BillListItemBinding
import com.project.how.data_class.dto.recode.receipt.ReceiptSimpleList
import kotlin.math.roundToLong

class BillListAdapter (
    data : ReceiptSimpleList,
    private val context : Context,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<BillListAdapter.ViewHolder>(){
    private var bills = data.toMutableList()
    inner class ViewHolder(val binding: BillListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: ReceiptListItem, position: Int){
            binding.title.text = data.scheduleName
            binding.date.text = "${data.startDate} - ${data.endDate}"
            binding.cost.text = context.getString(R.string.total_cost) + " ${(data.totalPrice*100).roundToLong().toDouble()/100} ${data.currency}"
            binding.count.text = context.getString(R.string.bill_count, data.count.toString())

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

    fun update(newData : ReceiptSimpleList){
        bills = newData.toMutableList()
        notifyDataSetChanged()
    }

    fun delete(position: Int){
        bills.removeAt(position)
        notifyItemRangeChanged(position, bills.lastIndex)
    }

    fun add(newData : ReceiptListItem){
        bills.add(newData)
        notifyItemInserted(bills.lastIndex)
    }

    interface OnItemClickListener{
        fun onItemClickListener(id: Long)
        fun onDeleteButtonClickListener(id: Long, position: Int)
    }
}