package com.project.how.adapter.recyclerview.schedule

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.how.data_class.dto.schedule.AddCheckListsRequest
import com.project.how.data_class.dto.schedule.AddCheckListsRequestElement
import com.project.how.data_class.dto.schedule.CheckList
import com.project.how.data_class.dto.schedule.CheckListElement
import com.project.how.databinding.ChecklistItemBinding

class ChecklistAdapter(
    data: CheckList, private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ChecklistAdapter.ViewHolder>() {
    private var checkList = data.toMutableList()
    private var updatedList = MutableList(checkList.size) { false }
    private var edit = false

    inner class ViewHolder(val binding: ChecklistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentTextWatcher: TextWatcher? = null

        fun bind(data: CheckListElement, position: Int) {
            if (edit) {
                ableEdit(binding)
            } else {
                unableEdit(binding)
            }
            if (data.id == -1L && data.todo.startsWith("HintText")) {
                binding.editText.setHint(data.todo.replace("HintText", ""))
                binding.editText.text = null
            } else {
                binding.editText.setText(data.todo)
            }

            binding.checkBox.isChecked = data.check
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                data.check = isChecked
                updatedList[position] = true
            }
            binding.delete.setOnClickListener {
                onItemClickListener.onDeleteButtonClickListener(data.id, position)
            }

            currentTextWatcher?.let { binding.editText.removeTextChangedListener(it) }

            currentTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrBlank()) {
                        data.todo = binding.editText.text.toString()
                    }
                }
            }
            binding.editText.addTextChangedListener(currentTextWatcher)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ChecklistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = checkList.size
    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = checkList[position]
        holder.bind(data, position)
    }

    fun getUpdatedCheckList(): CheckList {
        return checkList.filterIndexed { index, _ -> updatedList[index] }.toMutableList()
    }

    fun getNewCheckList(): AddCheckListsRequest {
        val newData = checkList.filter { it.id == -1L && !it.todo.startsWith("HintText") }
        return newData.map { AddCheckListsRequestElement(it.todo, it.check) }.toMutableList()
    }

    fun update(newData: CheckList) {
        checkList = newData.toMutableList()
        notifyDataSetChanged()
        updatedList = MutableList(checkList.size) { false }
    }

    fun add() {
        checkList.add(CheckListElement(-1, "HintText체크리스트를 생성해보세요.", false))
        updatedList.add(false)
        notifyItemInserted(checkList.lastIndex)
    }

    fun remove(position: Int) {
        checkList.removeAt(position)
        updatedList.removeAt(position)
        notifyDataSetChanged()
    }

    fun editStart() {
        edit = true
        notifyDataSetChanged()
    }

    fun editFinish() {
        edit = false
        notifyDataSetChanged()
    }

    private fun unableEdit(binding: ChecklistItemBinding) {
        binding.delete.visibility = View.GONE
        binding.editText.inputType = InputType.TYPE_NULL
    }

    private fun ableEdit(binding: ChecklistItemBinding) {
        binding.delete.visibility = View.VISIBLE
        binding.editText.inputType = InputType.TYPE_CLASS_TEXT
    }

    interface OnItemClickListener {
        fun onDeleteButtonClickListener(checklistId: Long, position: Int)
    }
}