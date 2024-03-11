package com.project.how.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.databinding.SimpleRadiobuttonItemBinding
import com.project.how.view.dialog.bottom_sheet_dialog.DesBottomSheetDialog
import kotlin.properties.Delegates

class RadioButtonAdapter(data: List<String>, private val multiple : Boolean, private val onItemClickListener: OnItemClickListener, private val type: Int)
    : RecyclerView.Adapter<RadioButtonAdapter.ViewHolder>(){
    private var data= data
    private var check = mutableListOf<Boolean>()

    init {
        if (check.isEmpty()){
            for(i in data.indices){
                check.add(false)
            }
        }
    }

    inner class ViewHolder(val binding : SimpleRadiobuttonItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: String, position: Int){
            binding.radioBtn.text = data
            binding.radioBtn.setOnClickListener {
                if (!multiple){
                    reset()
                    onItemClickListener.onItemClickListener(data, type)
                }
                check[position] = !check[position]
                notifyDataSetChanged()
            }
            changeColor(binding, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        SimpleRadiobuttonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data, position)
    }

    fun reset(){
        for (i in check.indices)
            check[i] = false
    }

    fun getData(position: Int) : String = data[position]

    fun getDatas() : List<String>{
        val datas= mutableListOf<String>()
        for (i in check.indices){
            if (check[i])
                datas.add(data[i])
        }
        return datas
    }

    private fun changeColor(binding: SimpleRadiobuttonItemBinding, position: Int){
        binding.radioBtn.isChecked = check[position]
    }

    interface OnItemClickListener{
        fun onItemClickListener(data: String, type : Int)
    }companion object{
        const val JAPAN = 1
        const val EUROPE = 2
        const val AMERICA = 3
        const val CANADA = 4
        const val SOUTHEAST_ASIA =5
        const val WHO = 101
        const val ACTIVITY_LEVEL = 102
        const val THEME = 103
    }
}