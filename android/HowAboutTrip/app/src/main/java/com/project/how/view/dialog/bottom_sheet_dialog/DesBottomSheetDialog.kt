package com.project.how.view.dialog.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.how.R
import com.project.how.adapter.RadioButtonAdapter
import com.project.how.databinding.DesBottomSheetBinding
import com.project.how.interface_af.OnDesListener
import com.project.how.view.dialog.ConfirmDialog
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext

class DesBottomSheetDialog(private val onDesListener: OnDesListener) : BottomSheetDialogFragment(), RadioButtonAdapter.OnItemClickListener  {
    private var _binding: DesBottomSheetBinding? = null
    private val binding: DesBottomSheetBinding
        get() = _binding!!
    private val japanPlaces : MutableList<String> = mutableListOf()
    private val europePlaces : MutableList<String> = mutableListOf()
    private val americaPlaces : MutableList<String> = mutableListOf()
    private val canadaPlaces : MutableList<String> = mutableListOf()
    private val southeastAsiaPlaces : MutableList<String> = mutableListOf()
    private lateinit var japanAdapter : RadioButtonAdapter
    private lateinit var europeAdapter : RadioButtonAdapter
    private lateinit var americaAdapter : RadioButtonAdapter
    private lateinit var canadaAdapter : RadioButtonAdapter
    private lateinit var southeastAsiaAdapter: RadioButtonAdapter
    private var des : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            japanPlaces.add(resources.getString(R.string.tokyo))
            japanPlaces.add(resources.getString(R.string.kyoto))
            japanPlaces.add(resources.getString(R.string.osaka))
            japanPlaces.add(resources.getString(R.string.fukuoka))
            japanPlaces.add(resources.getString(R.string.yufuin))
            japanPlaces.add(resources.getString(R.string.sapporo))
            japanPlaces.add(resources.getString(R.string.okinawa))

            japanAdapter = RadioButtonAdapter(japanPlaces, false,this@DesBottomSheetDialog, RadioButtonAdapter.JAPAN)
        }

        lifecycleScope.launch {
            europePlaces.add(resources.getString(R.string.england))
            europePlaces.add(resources.getString(R.string.france))
            europePlaces.add(resources.getString(R.string.germany))
            europePlaces.add(resources.getString(R.string.swiss))
            europePlaces.add(resources.getString(R.string.austria))
            europePlaces.add(resources.getString(R.string.italy))
            europePlaces.add(resources.getString(R.string.spain))
            europeAdapter = RadioButtonAdapter(europePlaces, false, this@DesBottomSheetDialog, RadioButtonAdapter.EUROPE)
        }

        lifecycleScope.launch {
            americaPlaces.add(getString(R.string.newyork))
            americaPlaces.add(getString(R.string.washington))
            americaPlaces.add(getString(R.string.hawaii))
            americaPlaces.add(getString(R.string.san_francisco))
            americaPlaces.add(getString(R.string.los_angeles))
            americaPlaces.add(getString(R.string.miami))

            americaAdapter = RadioButtonAdapter(americaPlaces, false,this@DesBottomSheetDialog, RadioButtonAdapter.AMERICA)
        }

        lifecycleScope.launch {
            canadaPlaces.add(getString(R.string.vancouver))
            canadaPlaces.add(getString(R.string.toronto))
            canadaPlaces.add(getString(R.string.quebec))
            canadaAdapter = RadioButtonAdapter(canadaPlaces, false, this@DesBottomSheetDialog, RadioButtonAdapter.CANADA)
        }

        lifecycleScope.launch {
            southeastAsiaPlaces.add(getString(R.string.philippines))
            southeastAsiaPlaces.add(getString(R.string.vietnam))
            southeastAsiaPlaces.add(getString(R.string.indonesia))
            southeastAsiaPlaces.add(getString(R.string.singapore))
            southeastAsiaPlaces.add(getString(R.string.cambodia))
            southeastAsiaPlaces.add(getString(R.string.malaysia))
            southeastAsiaAdapter = RadioButtonAdapter(southeastAsiaPlaces, false,this@DesBottomSheetDialog, RadioButtonAdapter.SOUTHEAST_ASIA)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.des_bottom_sheet, container, false)
        binding.des = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.japanPlaces.adapter = japanAdapter
        binding.europePlaces.adapter = europeAdapter
        binding.americaPlaces.adapter = americaAdapter
        binding.canadaPlaces.adapter = canadaAdapter
        binding.southeastAsiaPlaces.adapter = southeastAsiaAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onItemClickListener(data: String, type: Int) {
        when(type){
            RadioButtonAdapter.JAPAN->{
                lifecycleScope.launch {
                    des = data
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
                    canadaAdapter.reset()
                    canadaAdapter.notifyDataSetChanged()
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
                    canadaAdapter.reset()
                    canadaAdapter.notifyDataSetChanged()
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
                    canadaAdapter.reset()
                    canadaAdapter.notifyDataSetChanged()
                    southeastAsiaAdapter.reset()
                    southeastAsiaAdapter.notifyDataSetChanged()
                }
            }
            RadioButtonAdapter.CANADA->{
                lifecycleScope.launch{
                    des = data
                    japanAdapter.reset()
                    japanAdapter.notifyDataSetChanged()
                    europeAdapter.reset()
                    europeAdapter.notifyDataSetChanged()
                    americaAdapter.reset()
                    americaAdapter.notifyDataSetChanged()
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
                    canadaAdapter.reset()
                    canadaAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}