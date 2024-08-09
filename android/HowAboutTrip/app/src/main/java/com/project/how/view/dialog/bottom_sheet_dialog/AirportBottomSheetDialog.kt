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
import com.project.how.databinding.AirportBottomSheetBinding
import com.project.how.interface_af.interface_ff.OnAirportListener
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.bottom_sheet_dialog.ratio.BottomSheetRatioHeightManager
import kotlinx.coroutines.launch

class AirportBottomSheetDialog(private val type: Int, private val onAirportListener: OnAirportListener) : BottomSheetDialogFragment(), RadioButtonAdapter.OnItemClickListener {
    private var _binding : AirportBottomSheetBinding? = null
    private val binding : AirportBottomSheetBinding
        get() = _binding!!
    private lateinit var koreaAirports : MutableList<String>
    private lateinit var japanAirports : MutableList<String>
    private lateinit var europeAirports : MutableList<String>
    private lateinit var americaAirports : MutableList<String>
    private lateinit var southeastAsiaAirports : MutableList<String>
    private lateinit var koreaAdapter: RadioButtonAdapter
    private lateinit var japanAdapter : RadioButtonAdapter
    private lateinit var europeAdapter : RadioButtonAdapter
    private lateinit var americaAdapter : RadioButtonAdapter
    private lateinit var southeastAsiaAdapter: RadioButtonAdapter
    private var airport : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            koreaAirports = mutableListOf(
                getString(R.string.inchon),
                getString(R.string.gimpo)
            )

            japanAirports = mutableListOf(
                getString(R.string.tokyo),
                getString(R.string.osaka),
                getString(R.string.fukuoka),
                getString(R.string.sapporo),
                getString(R.string.okinawa),
                getString(R.string.kyushu))

            europeAirports = mutableListOf(
                getString(R.string.england),
                getString(R.string.france),
                getString(R.string.germany),
                getString(R.string.swiss),
                getString(R.string.italy),
                getString(R.string.spain),
                getString(R.string.austria),
                getString(R.string.czech)
            )

            americaAirports = mutableListOf(
                getString(R.string.newyork),
                getString(R.string.washington),
                getString(R.string.hawaii),
                getString(R.string.san_francisco),
                getString(R.string.los_angeles),
                getString(R.string.saipan),
                getString(R.string.guam)
            )

            southeastAsiaAirports = mutableListOf(
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

            koreaAdapter = RadioButtonAdapter(koreaAirports, false, this@AirportBottomSheetDialog, RadioButtonAdapter.KOREA)
            japanAdapter = RadioButtonAdapter(japanAirports, false, this@AirportBottomSheetDialog, RadioButtonAdapter.JAPAN)
            europeAdapter = RadioButtonAdapter(europeAirports, false, this@AirportBottomSheetDialog, RadioButtonAdapter.EUROPE)
            americaAdapter = RadioButtonAdapter(americaAirports, false, this@AirportBottomSheetDialog, RadioButtonAdapter.AMERICA)
            southeastAsiaAdapter = RadioButtonAdapter(southeastAsiaAirports, false, this@AirportBottomSheetDialog, RadioButtonAdapter.SOUTHEAST_ASIA)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.airport_bottom_sheet, container, false)
        binding.airport = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.koreaAirports.adapter = koreaAdapter
        binding.japanAirports.adapter = japanAdapter
        binding.europeAirports.adapter = europeAdapter
        binding.americaAirports.adapter = americaAdapter
        binding.southeastAsiaAirports.adapter = southeastAsiaAdapter
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            BottomSheetRatioHeightManager().setRatio(bottomSheetDialog, requireContext(), 90)
        }
        return binding.root
    }

    fun confirm(){
        if (airport == null){
            val message = listOf<String>(resources.getString(R.string.destination))
            val confirm = ConfirmDialog(message)
            confirm.show(childFragmentManager, "ConfirmDialog")
        }else{
            onAirportListener.onAirportListener(type, airport!!)
            dismiss()
        }
    }

    override fun onItemClickListener(data: String, type: Int, position: Int) {
        lifecycleScope.launch {
            when(type){
                RadioButtonAdapter.KOREA->{
                    airport = data
                    japanAdapter.reset()
                    japanAdapter.notifyDataSetChanged()
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
                RadioButtonAdapter.JAPAN->{
                    airport = data
                    koreaAdapter.reset()
                    koreaAdapter.notifyDataSetChanged()
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
                RadioButtonAdapter.EUROPE->{
                    airport = data
                    koreaAdapter.reset()
                    koreaAdapter.notifyDataSetChanged()
                    japanAdapter.reset()
                    japanAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
                RadioButtonAdapter.AMERICA->{
                    airport = data
                    koreaAdapter.reset()
                    koreaAdapter.notifyDataSetChanged()
                    japanAdapter.reset()
                    japanAdapter.notifyDataSetChanged()
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
                RadioButtonAdapter.SOUTHEAST_ASIA->{
                    airport = data
                    koreaAdapter.reset()
                    koreaAdapter.notifyDataSetChanged()
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

    companion object{
        const val DEPARTURE = 1
        const val DESTINATION = 2
    }
}