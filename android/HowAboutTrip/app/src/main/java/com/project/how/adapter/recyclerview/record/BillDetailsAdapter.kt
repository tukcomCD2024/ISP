package com.project.how.adapter.recyclerview.record

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.data_class.dto.recode.receipt.ReceiptDetailListItem
import com.project.how.databinding.BillDetailsItemBinding

class BillDetailsAdapter(
    private var details : MutableList<ReceiptDetailListItem>,
    private val currency : String,
    private val onPriceListener: OnPriceListener
) : RecyclerView.Adapter<BillDetailsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : BillDetailsItemBinding) : RecyclerView.ViewHolder(binding.root){
        private var currentTextWatcher: TextWatcher? = null
        private var titleTextWatcher: TextWatcher? = null

        fun bind(data : ReceiptDetailListItem, position : Int){
            if (data.title.startsWith("Hint")){
                binding.title.setHint(data.title.replace("Hint", ""))
            }else{
                binding.title.setText(data.title)
            }

            if (data.itemPrice == 0.0){
                binding.totalPrice.text = 0.toString()
            }else{
                binding.totalPrice.text = (data.count*data.itemPrice).toString()
            }
            binding.num.text = data.count.toString()
            binding.unitPrice.setText(data.itemPrice.toString())
            binding.currency.text = currency
            binding.totalCurrency.text = currency

            if (data.count == 1L){
                binding.minus.visibility = View.GONE
            }else{
                binding.minus.visibility = View.VISIBLE
            }

            onPriceListener.onTotalPriceListener(getTotalPrice())


            binding.minus.setOnClickListener {
                minus(data, position)
            }
            binding.plus.setOnClickListener {
                plus(data, position)
            }
            binding.delete.setOnClickListener {
                remove(position)
            }

            currentTextWatcher?.let { binding.unitPrice.removeTextChangedListener(it) }
            titleTextWatcher?.let { binding.title.removeTextChangedListener(it) }

            currentTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrBlank()) {
                        val cursorPosition = binding.unitPrice.selectionStart
                        data.itemPrice = s.toString().toDoubleOrNull() ?: 0.0
                        binding.unitPrice.setSelection(cursorPosition)
                        binding.totalPrice.text = (data.count*data.itemPrice).toString()
                        onPriceListener.onTotalPriceListener(getTotalPrice())
                    }
                }
            }
            binding.unitPrice.addTextChangedListener(currentTextWatcher)

            titleTextWatcher = object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    data.title = binding.title.text.toString()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        BillDetailsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = details.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = details[position]
        holder.bind(data, position)
    }

    private fun plus(data : ReceiptDetailListItem, position: Int){
        data.count++
        notifyItemChanged(position)
    }

    private fun minus(data : ReceiptDetailListItem, position: Int){
        if (data.count > 1) {
            data.count--
            notifyItemChanged(position)
        }
    }

    private fun remove(position: Int){
        details.removeAt(position)
        notifyItemRemoved(position)
    }

    fun update(newData : List<ReceiptDetailListItem>){
        details = newData.toMutableList()
        notifyDataSetChanged()
    }

    fun add(){
        details.add(ReceiptDetailListItem(
            "Hint물품 이름",
            1L,
            1000.0
        ))
        notifyItemInserted(details.lastIndex)
    }

    fun getTotalPrice() : Double = details.sumOf { it.itemPrice * it.count }

    fun getAllData() : List<ReceiptDetailListItem> = details.toList()

    interface OnPriceListener{
        fun onTotalPriceListener(total : Double)
    }
}
