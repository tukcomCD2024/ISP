package com.project.how.view.fragment.record

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.project.how.R
import com.project.how.adapter.recyclerview.record.BillDetailsAdapter
import com.project.how.data_class.dto.recode.receipt.ReceiptDetail
import com.project.how.data_class.dto.recode.receipt.ReceiptDetailListItem
import com.project.how.databinding.FragmentOcrBinding
import com.project.how.view.activity.record.BillInputActivity
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view_model.RecordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


class OcrFragment : Fragment(), BillDetailsAdapter.OnPriceListener {
    private var _binding : FragmentOcrBinding? = null
    private val binding : FragmentOcrBinding
        get() = _binding!!
    private val recordViewModel : RecordViewModel by viewModels()
    private lateinit var adapter : BillDetailsAdapter
    private lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>
    private val args: OcrFragmentArgs by navArgs()
    private lateinit var data : ReceiptDetail
    private lateinit var currentDate : String
    private lateinit var currency : String
    private var camera = false
    private var id = -1L
    private var totalPrice = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDate = args.date
        currency = args.currency
        camera = args.camera
        id = args.id

        Log.d("OcrFragment", "currentDate : $currentDate\ncurrency : $currency\ncamera : $camera\nid : $id")

        adapter = BillDetailsAdapter(mutableListOf<ReceiptDetailListItem>(), currency, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ocr, container, false)
        binding.ocr = this
        binding.lifecycleOwner = viewLifecycleOwner
        binding.details.adapter = adapter
        binding.date.text = currentDate
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                lock()
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.image.setImageURI(uri)
                recordViewModel.uploadReceipt(requireContext(), uri)

            } else {
                Log.d("PhotoPicker", "No media selected")
                unlock()
            }
        }
        recordViewModel.ocrResponseLiveData.observe(viewLifecycleOwner){ocrResult->
            lifecycleScope.launch {
                unlock()
                if (ocrResult != null) {
                    recordViewModel.getReceiptDetail(id, currentDate, ocrResult)?.let {
                        data = it
                        adapter.update(data.receiptDetails)
                        setUI(data)
                    } ?: {
                        Toast.makeText(requireContext(),
                            getString(R.string.not_supported_image_type), Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),
                        getString(R.string.ocr_fail_message), Toast.LENGTH_LONG).show()
                }
            }
        }

        imageInit()

        return binding.root
    }

    fun putImage(){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun lock(){
        binding.loadingBackground.visibility = View.VISIBLE
        binding.loadingLottie.visibility = View.VISIBLE
        binding.loadingInfo.visibility = View.VISIBLE
        binding.loadingLottie.playAnimation()

        binding.title.inputType = InputType.TYPE_NULL
        binding.save.isEnabled = false
        binding.add.isEnabled = false
        binding.image.isEnabled = false
    }

    private fun unlock(){
        binding.loadingBackground.visibility = View.GONE
        binding.loadingLottie.visibility = View.GONE
        binding.loadingInfo.visibility = View.GONE
        binding.loadingLottie.cancelAnimation()

        binding.title.inputType = InputType.TYPE_CLASS_TEXT
        binding.save.isEnabled = true
        binding.add.isEnabled = true
        binding.image.isEnabled = true
    }

    fun setUI(receiptDetail: ReceiptDetail){
        binding.title.setText(receiptDetail.storeName)
        binding.date.text = currentDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun add(){
        adapter.add()
    }

    fun save(){
        lifecycleScope.launch {
            try {
                val storeName = binding.title.text.toString()
                if (storeName.isNullOrBlank()) data.storeName = getString(R.string.empty_title) else data.storeName = storeName
                data.totalPrice = totalPrice
                recordViewModel.saveReceipt(requireContext(), data).join()
                val activity = requireActivity() as? BillInputActivity
                activity?.closeAllFragmentsAndFinishActivity()
            }catch (e : Exception){
                ConfirmDialog(listOf(getString(R.string.image))).show(childFragmentManager, "ConfirmDialog")
            }
        }
    }

    private fun imageInit(){
        if (camera){

        }else{
            putImage()
        }
    }

    override fun onTotalPriceListener(total: Double) {
        val formatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(total)
        binding.total.text = getString(R.string.bill_total_price, formatted, currency)
        totalPrice = total
    }

}