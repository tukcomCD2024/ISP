package com.project.how.adapter.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
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
    private val onItemClickListener: OnItemClickListener
)
    : RecyclerView.Adapter<DaysScheduleEditAdapter.ViewHolder>(), ItemMoveListener, PopupMenu.OnMenuItemClickListener {
    private var dailySchedule = data
    private var initList: MutableList<DaysSchedule> = mutableListOf()
    private var onItemDragListener: ItemStartDragListener? = null
    private var currentPosition = -1
    private var currentData : DaysSchedule? = null

    inner class ViewHolder(val binding: CalendarDaysScheduleEditItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : DaysSchedule, position: Int){
            binding.scheduleTitle.text = data.todo
            val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.cost)
            binding.budget.text = context.getString(R.string.budget, formattedNumber)

            if ((data.latitude == null || data.longitude == null) || (data.latitude == 0.0 && data.longitude == 0.0)){
              binding.perfect.visibility = View.GONE
            }else{
                binding.perfect.visibility = View.VISIBLE
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

            binding.more.setOnClickListener {
                currentPosition = position
                currentData = data
                PopupMenu(context, binding.more).apply {
                    inflate(R.menu.schedule_edit_more)
                    setOnMenuItemClickListener(this@DaysScheduleEditAdapter)
                    show()
                }
            }

            binding.edit.setOnClickListener {
                onItemClickListener.onEditButtonClickListener(data, position)
            }
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_sch_order_change -> {
                onItemClickListener.onMoreMenuOrderChangeClickListener(currentPosition)
            }
            R.id.menu_sch_date_change -> {
                onItemClickListener.onMoreMenuDateChangeClickListener(currentData!!, currentPosition)
            }
            R.id.menu_sch_delete -> {
                onItemClickListener.onMoreMenuDeleteClickListener(currentPosition)
            }
            else ->{
                Toast.makeText(context, context.getString(R.string.non_exist_menu_warning), Toast.LENGTH_SHORT).show()
            }
        }
        return false
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

    fun remove(position: Int, notify: Boolean){
        dailySchedule.removeAt(position)
        if (notify)
            notifyDataSetChanged()
    }

    fun edit(data : DaysSchedule, position: Int){
        dailySchedule[position] = data
        notifyItemChanged(position)
    }

    fun swap(fromPosition: Int, toPosition: Int){
        val temp = dailySchedule[fromPosition]
        dailySchedule[fromPosition] = dailySchedule[toPosition]
        dailySchedule[toPosition] = temp
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getData() = dailySchedule

    fun getData(position: Int) = dailySchedule[position]

    fun add(position: Int, data: DaysSchedule, notify : Boolean){
        dailySchedule.add(position, data)
        if (notify)
            notifyDataSetChanged()
    }



    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Log.d("addOnItemTouchListener", "onItemMove start\nfrom : $fromPosition\tto : $toPosition")
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dailySchedule, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dailySchedule, i, i - 1)
            }
        }
        Log.d("addOnItemTouchListener", "onItemMove if for end")
        notifyItemMoved(fromPosition, toPosition)
        Log.d("addOnItemTouchListener", "onItemMove notifyItemMoved end")
        return true
    }

    override fun onDropAdapter(fromPosition: Int, toPosition: Int) : Boolean {
        onItemDragListener?.onDropActivity(initList, dailySchedule)
        return true
    }

    interface OnItemClickListener{
        fun onEditButtonClickListener(data : DaysSchedule, position: Int)
        fun onMoreMenuDateChangeClickListener(data : DaysSchedule, position: Int)
        fun onMoreMenuOrderChangeClickListener(position: Int)
        fun onMoreMenuDeleteClickListener(position: Int)
    }

    companion object{
        const val AIRPLANE = 0
        const val HOTEL = 1
        const val PLACE = 2
    }
}