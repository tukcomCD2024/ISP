package com.project.how.adapter.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.R
import com.project.how.data_class.recyclerview.DaysSchedule
import com.project.how.databinding.CalendarDaysScheduleEditItemBinding
import com.project.how.interface_af.interface_ada.ItemMoveListener
import com.project.how.interface_af.interface_ada.ItemStartDragListener
import java.text.NumberFormat
import java.util.Collections
import java.util.Locale

class DaysScheduleEditAdapter (
    data: MutableList<DaysSchedule>,
    private val context: Context,
    private val onButtonClickListener: OnDaysButtonClickListener
)
    : RecyclerView.Adapter<DaysScheduleEditAdapter.ViewHolder>(), ItemMoveListener{
    private var dailySchedule = data
    private var initList: MutableList<DaysSchedule> = mutableListOf()
    private var onItemDragListener: ItemStartDragListener? = null

    inner class ViewHolder(val binding: CalendarDaysScheduleEditItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : DaysSchedule, position: Int){
            binding.scheduleTitle.text = data.todo
            val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.cost)
            binding.budget.text = context.getString(R.string.budget, formattedNumber)

            if ((data.latitude == null || data.longitude == null) || (data.latitude == 0.0 && data.longitude == 0.0)){
              binding.editNeed.visibility = View.VISIBLE
            }else{
                binding.editNeed.visibility = View.GONE
            }

            if (data.purchaseStatus){
                binding.date.visibility = View.VISIBLE
                binding.date.text = data.purchaseDate
            }

            if (position == 0)
                binding.topDottedLine.visibility = View.GONE
            else
                binding.topDottedLine.visibility = View.VISIBLE

            when(data.type){
                AIRPLANE ->{
                    binding.edit.visibility = View.VISIBLE
                    binding.number.visibility = View.GONE
                    binding.numberBackground.setImageResource(R.drawable.icon_ticket_bold)
                }
                HOTEL ->{
                    binding.number.text = "${position+1}"
                    binding.number.visibility = View.VISIBLE
                    binding.edit.visibility = View.VISIBLE
                    binding.numberBackground.setImageResource(R.drawable.black_oval)

                }
                PLACE ->{
                    binding.number.text = "${position+1}"
                    binding.number.visibility = View.VISIBLE
                    binding.edit.visibility = View.VISIBLE
                    binding.numberBackground.setImageResource(R.drawable.black_oval)
                }
            }

            binding.delete.setOnClickListener {
                remove(position)
                onButtonClickListener.onDeleteButtonClickListener(position)
            }

            binding.edit.setOnClickListener {
                onButtonClickListener.onEditButtonClickListener(data, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        CalendarDaysScheduleEditItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun getItemCount(): Int = dailySchedule.size

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dailySchedule[position]
        holder.bind(data, position)
    }


    fun itemDragListener(startDrag: ItemStartDragListener) {
        this.onItemDragListener = startDrag
    }

    fun update(data : MutableList<DaysSchedule>){
        dailySchedule = data
        notifyDataSetChanged()
    }

    fun add(newData : DaysSchedule){
        dailySchedule.add(newData)
        notifyItemChanged(dailySchedule.lastIndex)
    }

    fun remove(position: Int){
        dailySchedule.removeAt(position)
        notifyDataSetChanged()
    }

    fun edit(data : DaysSchedule, position: Int){
        dailySchedule[position] = data
        notifyItemChanged(position)
    }

    fun getData() = dailySchedule

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(dailySchedule, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onDropAdapter() {
        onItemDragListener?.onDropActivity(initList, dailySchedule)
    }

    interface OnDaysButtonClickListener{
        fun onEditButtonClickListener(data : DaysSchedule, position: Int)
        fun onDeleteButtonClickListener(position : Int)
    }

    companion object{
        const val AIRPLANE = 0
        const val HOTEL = 1
        const val PLACE = 2
    }
}