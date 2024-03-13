package com.project.how.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.R
import com.project.how.data_class.DaysSchedule
import com.project.how.databinding.CalendarDaysScheduleItemBinding
import com.project.how.interface_af.interface_ada.ItemMoveListener
import com.project.how.interface_af.interface_ada.ItemStartDragListener
import java.text.NumberFormat
import java.util.Collections
import java.util.Locale

class DaysScheduleAdapter (data: MutableList<DaysSchedule>, private val context: Context)
    : RecyclerView.Adapter<DaysScheduleAdapter.ViewHolder>(), ItemMoveListener{
    private var dailySchedule = data
    private var totalCost : Long = 0
    private var onItemDragListener: ItemStartDragListener? = null
    var initList: MutableList<DaysSchedule> = mutableListOf()

    inner class ViewHolder(val binding: CalendarDaysScheduleItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : DaysSchedule, position: Int){
            binding.scheduleTitle.text = data.todo
            totalCost += data.cost
            val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.cost)
            binding.budget.text = context.getString(R.string.budget, formattedNumber)

            if (data.latitude == null || data.longitude == null){
              binding.editNeed.visibility = View.VISIBLE
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
                AIRPLANE->{
                    binding.buy.visibility = View.VISIBLE
                    binding.number.visibility = View.GONE
                    binding.numberBackground.setImageResource(R.drawable.icon_ticket_bold)
                }
                HOTEL->{
                    binding.number.text = "${position+1}"
                    binding.number.visibility = View.VISIBLE
                    binding.buy.visibility = View.VISIBLE
                    binding.numberBackground.setImageResource(R.drawable.black_oval_day_background)

                }
                PLACE->{
                    binding.number.text = "${position+1}"
                    binding.number.visibility = View.VISIBLE
                    binding.buy.visibility = View.VISIBLE
                    binding.numberBackground.setImageResource(R.drawable.black_oval_day_background)
                }
            }

            binding.delete.setOnClickListener {
                remove(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        CalendarDaysScheduleItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))

    override fun getItemCount(): Int = dailySchedule.size

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

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(dailySchedule, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onDropAdapter() {
        onItemDragListener?.onDropActivity(initList, dailySchedule)
    }

    companion object{
        const val AIRPLANE = 0
        const val HOTEL = 1
        const val PLACE = 2
    }
}