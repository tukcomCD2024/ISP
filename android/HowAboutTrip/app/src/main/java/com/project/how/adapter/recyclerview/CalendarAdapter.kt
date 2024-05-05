package com.project.how.adapter.recyclerview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.R
import com.project.how.databinding.CalendarDayItemBinding
import java.time.LocalDate

class CalendarAdapter(
    repositoryDays : List<Int>,
    selectedDate : LocalDate,
    private val onItemClickListener : OnItemClickListener
)
    : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var sd = selectedDate
    private var selected : MutableList<Boolean> = mutableListOf()
    private var days = repositoryDays
    private var emptyCount = 0
    private var dayCount = 0

    init {
        Log.d("CalendarAdapter", "init repositoryDays.size : ${repositoryDays.size}\n${days.size}")
        initSelected()
    }

    inner class ViewHolder(val binding : CalendarDayItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Int, position: Int){
            if(data != EMPTY){
                binding.day.text = dayCount++.toString()
                binding.day.setOnClickListener {
                    val dayOfMonthNum = position-emptyCount+1
                    val selectedDay = sd.withDayOfMonth(dayOfMonthNum)
                    onItemClickListener.onItemClickListener(selected, selectedDay, position)
                }

                changeColor(binding, position)
            }else{
                binding.day.visibility = View.GONE
                emptyCount++
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        CalendarDayItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = days[position]
        holder.bind(data, position)
    }

    fun update(newDays : List<Int>, sd: LocalDate){
        days = newDays
        dayCount = 1
        emptyCount = 0
        observeMonthChange(sd)
        notifyDataSetChanged()
    }

    fun observeMonthChange(sd : LocalDate){
        this.sd = sd
        initSelected()
    }

    private fun initSelected(){
        Log.d("CalendarAdapter", "Before ${selected.size}\n${days.size}")
        selected = mutableListOf()
        for(i in days.indices){
            selected.add(false)
        }
        Log.d("CalendarAdapter", "After ${selected.size}\n${days.size}")
    }

    fun resetSelected(){
        for(i in days.indices){
            selected[i] = false
        }
        dayCount = 1
        emptyCount = 0
        notifyDataSetChanged()
    }

    private fun changeColor(binding: CalendarDayItemBinding, position: Int) {
        if (selected[position] && days[position] != EMPTY) {
            binding.day.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.background.setBackgroundResource(R.drawable.black_oval)
        } else {
            binding.day.setTextColor(Color.parseColor("#FF000000"))
            binding.background.setBackgroundResource(R.drawable.white_oval)
        }
    }

    interface OnItemClickListener{
        fun onItemClickListener(selected : MutableList<Boolean>, sd: LocalDate, position: Int)
    }
    companion object{
        private const val EMPTY = 99
    }
}