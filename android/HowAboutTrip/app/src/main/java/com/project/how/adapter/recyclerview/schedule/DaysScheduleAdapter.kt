package com.project.how.adapter.recyclerview.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.R
import com.project.how.data_class.recyclerview.schedule.DaysSchedule
import com.project.how.databinding.CalendarDaysScheduleItemBinding
import java.text.NumberFormat
import java.util.Locale

class DaysScheduleAdapter (
    data: MutableList<DaysSchedule>,
    private val context: Context,
    private val currency : String,
    private val onButtonClickListener: OnDaysButtonClickListener
)
    : RecyclerView.Adapter<DaysScheduleAdapter.ViewHolder>(){
    private var dailySchedule = data

    inner class ViewHolder(val binding: CalendarDaysScheduleItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : DaysSchedule, position: Int){
            binding.scheduleTitle.text = data.todo
            val formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.cost)
            binding.budget.text = context.getString(R.string.budget, formattedNumber, currency)

            if (data.purchaseStatus){
                binding.date.visibility = View.VISIBLE
                binding.date.text = data.purchaseDate
            }

            if (position == 0)
                binding.topDottedLine.visibility = View.GONE
            else
                binding.topDottedLine.visibility = View.VISIBLE

            if (position == dailySchedule.lastIndex)
                binding.bottomDottedLine.visibility = View.GONE
            else
                binding.bottomDottedLine.visibility = View.VISIBLE

            when(data.type){
                AIRPLANE ->{
                    binding.search.visibility = View.VISIBLE
                    binding.number.visibility = View.GONE
                    binding.numberBackground.setImageResource(R.drawable.icon_ticket_bold)
                }
                HOTEL ->{
                    binding.number.text = "${position+1}"
                    binding.number.visibility = View.VISIBLE
                    binding.search.visibility = View.VISIBLE
                    binding.numberBackground.setImageResource(R.drawable.black_oval)

                }
                PLACE ->{
                    binding.number.text = "${position+1}"
                    binding.number.visibility = View.VISIBLE
                    binding.search.visibility = View.GONE
                    binding.numberBackground.setImageResource(R.drawable.black_oval)
                }
            }

            binding.search.setOnClickListener {
                onButtonClickListener.onSearchButtonClickListener(data, position)
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

    fun update(data : MutableList<DaysSchedule>){
        dailySchedule = data
        notifyDataSetChanged()
    }

    interface OnDaysButtonClickListener{
        fun onSearchButtonClickListener(data : DaysSchedule, position: Int)
    }

    companion object{
        const val AIRPLANE = 0
        const val HOTEL = 1
        const val PLACE = 2
    }
}