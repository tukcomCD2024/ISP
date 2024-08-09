package com.project.how.view.dialog.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.R
import com.project.how.adapter.recyclerview.RadioButtonAdapter
import com.project.how.databinding.DesBottomSheetBinding
import com.project.how.interface_af.OnDesListener
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.bottom_sheet_dialog.ratio.BottomSheetRatioHeightManager
import kotlinx.coroutines.launch

class DesBottomSheetDialog(private val onDesListener: OnDesListener) : BottomSheetDialogFragment(), RadioButtonAdapter.OnItemClickListener  {
    private var _binding: DesBottomSheetBinding? = null
    private val binding: DesBottomSheetBinding
        get() = _binding!!
    private lateinit var japanPlaces : MutableList<String>
    private lateinit var europePlaces : MutableList<String>
    private lateinit var americaPlaces : MutableList<String>
    private lateinit var southeastAsiaPlaces : MutableList<String>
    private lateinit var japanAdapter : RadioButtonAdapter
    private lateinit var europeAdapter : RadioButtonAdapter
    private lateinit var americaAdapter : RadioButtonAdapter
    private lateinit var southeastAsiaAdapter: RadioButtonAdapter
    private var des : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            japanPlaces = mutableListOf(
                getString(R.string.tokyo),
                getString(R.string.osaka),
                getString(R.string.fukuoka),
                getString(R.string.sapporo),
                getString(R.string.okinawa),
                getString(R.string.kyushu))

            japanAdapter = RadioButtonAdapter(japanPlaces, false,this@DesBottomSheetDialog, RadioButtonAdapter.JAPAN)

            europePlaces = mutableListOf(
                getString(R.string.england),
                getString(R.string.france),
                getString(R.string.germany),
                getString(R.string.swiss),
                getString(R.string.italy),
                getString(R.string.spain),
                getString(R.string.austria),
                getString(R.string.czech)
            )
            europeAdapter = RadioButtonAdapter(europePlaces, false, this@DesBottomSheetDialog, RadioButtonAdapter.EUROPE)

            americaPlaces = mutableListOf(
                getString(R.string.newyork),
                getString(R.string.washington),
                getString(R.string.hawaii),
                getString(R.string.san_francisco),
                getString(R.string.los_angeles),
                getString(R.string.saipan),
                getString(R.string.guam)
            )

            americaAdapter = RadioButtonAdapter(americaPlaces, false,this@DesBottomSheetDialog, RadioButtonAdapter.AMERICA)

            southeastAsiaPlaces = mutableListOf(
                getString(R.string.singapore),
                getString(R.string.taiwan),
                getString(R.string.laos),
                getString (R.string.kota_kinabalu),
                getString(R.string.hanoi),
                getString(R.string.da_nang_and_hoi_an),
                getString(R.string.phu_quoc),
                getString(R.string.nha_trang_and_da_lat),
                getString(R.string.bangkok),
                getString(R.string.chiang_mai),
                getString(R.string.phuket),
                getString(R.string.bali),
                getString(R.string.borikai)
            )

            southeastAsiaAdapter = RadioButtonAdapter(southeastAsiaPlaces, false,this@DesBottomSheetDialog, RadioButtonAdapter.SOUTHEAST_ASIA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.des_bottom_sheet, container, false)
        binding.des = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.japanPlaces.adapter = japanAdapter
        binding.europePlaces.adapter = europeAdapter
        binding.americaPlaces.adapter = americaAdapter
        binding.southeastAsiaPlaces.adapter = southeastAsiaAdapter
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            BottomSheetRatioHeightManager().setRatio(bottomSheetDialog, requireContext(), 75)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun confirm(){
        if (des == null){
            val message = listOf<String>(resources.getString(R.string.destination))
            val confirm = ConfirmDialog(message)
            confirm.show(childFragmentManager, "ConfirmDialog")
        }else{
            onDesListener.onDesListener(des!!)
            dismiss()
        }
    }

    override fun onItemClickListener(data: String, type: Int, position: Int) {
        when(type){
            RadioButtonAdapter.JAPAN->{
                lifecycleScope.launch {
                    des = data
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
            }
            RadioButtonAdapter.EUROPE->{
                lifecycleScope.launch {
                    des = data
                    japanAdapter.reset()
                    japanAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
            }
            RadioButtonAdapter.AMERICA->{
                lifecycleScope.launch {
                    des = data
                    japanAdapter.reset()
                    japanAdapter.notifyDataSetChanged()
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
            }
            RadioButtonAdapter.SOUTHEAST_ASIA->{
                lifecycleScope.launch {
                    des = data
                    japanAdapter.reset()
                    japanAdapter.notifyDataSetChanged()
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}