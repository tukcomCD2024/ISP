package com.project.how.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.R
import com.project.how.databinding.CalendarDayItemBinding
import java.time.LocalDate

class CalendarAdapter(
    repositoryDays : List<Int>,
    selectedDate : LocalDate,
    private val onItemClickListener : OnItemClickListener)
    : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var sd = selectedDate
    private var selected : MutableList<Boolean> = mutableListOf()
    private val selectedMonthDays : MutableMap<Int, MutableList<Boolean>> = mutableMapOf()
    private val selectedTwoDay : MutableMap<Int, LocalDate> = mutableMapOf()
    private var days = repositoryDays
    private var emptyCount = 0
    private var dayCount = 1
    private var selectedCount = 0

    init {
        Log.d("CalendarAdapter", "init repositoryDays.size : ${repositoryDays.size}\n${days.size}")
        initSelected()
    }

    inner class ViewHolder(val binding : CalendarDayItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Int, position: Int){
            if(data != EMPTY){
                binding.day.text = dayCount++.toString()
                val firstSelected = selectedTwoDay[FIRST]
                val secondSelected = selectedTwoDay[SECOND]
                if(firstSelected != null && secondSelected != null){
                    if(firstSelected.month.value < secondSelected.month.value){
                        if((position - emptyCount) < secondSelected.dayOfMonth){
                            binding.day.setTextColor(Color.parseColor("#00000000"))
                            binding.background.setBackgroundResource(R.color.black)
                        }
                    }else if(firstSelected.month.value > secondSelected.month.value){
                        if((position - emptyCount) < firstSelected.dayOfMonth){
                            binding.day.setTextColor(Color.parseColor("#00000000"))
                            binding.background.setBackgroundResource(R.color.black)
                        }
                    }else{
                        if(firstSelected.dayOfMonth < secondSelected.dayOfMonth){
                            if((firstSelected.dayOfMonth < (position - emptyCount)) && (secondSelected.dayOfMonth > (position - emptyCount))) {
                                binding.day.setTextColor(Color.parseColor("#00000000"))
                                binding.background.setBackgroundResource(R.color.black)
                            }
                        }else if(firstSelected.dayOfMonth > secondSelected.dayOfMonth){
                            if((firstSelected.dayOfMonth > (position - emptyCount)) && (secondSelected.dayOfMonth < (position - emptyCount))) {
                                binding.day.setTextColor(Color.parseColor("#00000000"))
                                binding.background.setBackgroundResource(R.color.black)
                            }
                        }
                    }
                }
                binding.day.setOnClickListener {
//                    selected[position] = !selected[position]
                    selectedTwoDay[selectedCount++%2] = sd.withDayOfMonth(position-emptyCount)
                    val sd = sd.withDayOfMonth(position-emptyCount)
                    onItemClickListener.onItemClickListener(data, selected, position, sd)
                }

                changeColor(binding, position)
            }else{
                binding.day.text = " "
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
        selectedMonthDays[this.sd.month.value] = selected
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
        for(i in days.indices)
            selected[i] = false
        dayCount = 1
        emptyCount = 0
        notifyDataSetChanged()
    }

    private fun changeColor(binding: CalendarDayItemBinding, position: Int){
        if(selected[position]){
            binding.day.setTextColor(Color.parseColor("#FFFFFFFF"))
            binding.background.setBackgroundResource(R.drawable.black_oval_day_background)
        }else{
            binding.day.setTextColor(Color.parseColor("#FF000000"))
            binding.background.setBackgroundResource(R.drawable.white_oval_day_background)
        }
    }

    fun getSelectedDays() : MutableMap<Int, LocalDate>?{
        if (selectedTwoDay[FIRST] != null || selectedTwoDay[SECOND] != null)
            return selectedTwoDay
        return null
    }

    fun getSelectedDay() : LocalDate?{
        if (selectedTwoDay[FIRST] != null)
            return selectedTwoDay[FIRST]
        return null
    }

    interface OnItemClickListener{
        fun onItemClickListener(data : Int, selected : MutableList<Boolean>, position: Int, sd: LocalDate){}
    }
    companion object{
        private const val EMPTY = 99
        private const val FIRST = 0
        private const val SECOND = 1
    }
}