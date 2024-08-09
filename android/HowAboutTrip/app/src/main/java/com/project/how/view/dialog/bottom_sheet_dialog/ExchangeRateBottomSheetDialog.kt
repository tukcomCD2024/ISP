package com.project.how.view.dialog.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.R
import com.project.how.databinding.ExchageRateBottomSheetBinding
import com.project.how.interface_af.OnDesListener
import com.project.how.view.dialog.bottom_sheet_dialog.ratio.BottomSheetRatioHeightManager

class ExchangeRateBottomSheetDialog(private val formerDes : String, private val onDesListener: OnDesListener) : BottomSheetDialogFragment() {
    private var _binding : ExchageRateBottomSheetBinding? = null
    private val binding : ExchageRateBottomSheetBinding
        get() = _binding!!
    private lateinit var counties : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counties = listOf(
            getString(R.string.taiwan),
            getString(R.string.korea),
            getString(R.string.laos),
            getString(R.string.malaysia),
            getString(R.string.america),
            getString(R.string.vietnam),
            getString(R.string.swiss),
            getString(R.string.singapore),
            getString(R.string.euro),
            getString(R.string.england),
            getString(R.string.indonesia),
            getString(R.string.japan),
            getString(R.string.czech),
            getString(R.string.thailand),
            getString(R.string.philippines)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.exchage_rate_bottom_sheet, container, false)
        binding.exchange = this
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            BottomSheetRatioHeightManager().setRatio(bottomSheetDialog, requireContext(), 85)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!binding.countries.isEmpty()){
            binding.countries.removeAllViews()
        }
        setRadioButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRadioButton() {
        for(c in counties){
            val rdbtn = RadioButton(requireContext()).apply {
                text = c
                id = View.generateViewId()
                if (c == formerDes){
                    isChecked = true
                }
            }
            binding.countries.addView(rdbtn)
        }
    }

    private fun getCheckedRadioButtonDes() : String{
        val checked = binding.countries.checkedRadioButtonId
        if(checked == -1){
            return formerDes
        }

        val radioBtn = binding.countries.findViewById<RadioButton>(checked)
        return radioBtn.text.toString()
    }

    fun confirm(){
        val des = getCheckedRadioButtonDes()
        onDesListener.onDesListener(des)
        dismiss()
    }
}