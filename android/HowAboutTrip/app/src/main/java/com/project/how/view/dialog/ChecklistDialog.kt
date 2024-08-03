package com.project.how.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.project.how.R
import com.project.how.adapter.recyclerview.schedule.ChecklistAdapter
import com.project.how.data_class.dto.schedule.CheckListElement
import com.project.how.databinding.DialogChecklistBinding
import com.project.how.view_model.ScheduleViewModel
import kotlinx.coroutines.launch

class ChecklistDialog(private val scheduleId : Long) : DialogFragment(),
    ChecklistAdapter.OnItemClickListener {
    private val viewModel : ScheduleViewModel by viewModels()
    private var _binding : DialogChecklistBinding? = null
    private val binding : DialogChecklistBinding
        get() = _binding!!
    private lateinit var adapter : ChecklistAdapter
    private var editMode = false
    private var editable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_checklist, container, false)
        binding.checklist = this
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ChecklistAdapter(listOf<CheckListElement>(), this@ChecklistDialog)
        binding.check.adapter = adapter
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setOnShowListener {
            val layoutParams = dialog?.window?.attributes
            val displayMetrics = resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            layoutParams?.width = screenWidth - 50
            dialog?.window?.attributes = layoutParams
        }

        lifecycleScope.launch {
            viewModel.checkListLiveData.observe(viewLifecycleOwner){
                adapter.update(it)
                editable = true
            }
        }
        lifecycleScope.launch {
            viewModel.getCheckList(scheduleId).collect{
                if (it != ScheduleViewModel.SUCCESS){
                    Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun add(){
        adapter.add()
    }

    fun edit(){
        if (editable){
            editMode = true
            changeMode()
        }
    }

    fun confirm(){
        if (editMode){
            save()
            editMode = false
            changeMode()
        }else{
            close()
        }
    }

    private fun changeMode(){
        if (editMode){
            adapter.editStart()
            binding.edit.visibility = View.GONE
            binding.confirm.text = getString(R.string.save)
            binding.add.visibility = View.VISIBLE
        }else{
            adapter.editFinish()
            binding.edit.visibility = View.VISIBLE
            binding.confirm.text = getString(R.string.close)
            binding.add.visibility = View.GONE
        }
    }

    private fun save(){
        lifecycleScope.launch{
            val newChecklist = adapter.getNewCheckList()
            viewModel.addCheckList(scheduleId, newChecklist).collect{

            }
        }
    }

    private fun update(){
        try {
            val updated = adapter.getUpdatedCheckList()
            viewModel.updateCheckLists(scheduleId, updated)
        }catch (e : Exception){
            Log.e("ChecklistDialog", "update is failed\n${e.message}")
        }
    }

    override fun dismiss() {
        update()
        super.dismiss()
    }

    private fun close(){
        dismiss()
    }

    override fun onDeleteButtonClickListener(checklistId: Long, position: Int) {
        lifecycleScope.launch {
            if (checklistId == -1L){
                adapter.remove(position)
                return@launch
            }
            viewModel.deleteCheckList(scheduleId, checklistId).collect{
                if (it == ScheduleViewModel.SUCCESS){
                    adapter.remove(position)
                }
            }
        }
    }
}
