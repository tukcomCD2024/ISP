package com.project.how.view.fragment.record

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.record.BillDetailsAdapter
import com.project.how.data_class.dto.recode.receipt.ReceiptDetail
import com.project.how.data_class.dto.recode.receipt.ReceiptDetailListItem
import com.project.how.databinding.FragmentOcrBinding
import com.project.how.interface_af.OnYesOrNoListener
import com.project.how.view.activity.record.BillActivity
import com.project.how.view.activity.record.BillInputActivity
import com.project.how.view.dialog.ConfirmDialog
import com.project.how.view.dialog.WarningDialog
import com.project.how.view.dialog.YesOrNoDialog
import com.project.how.view_model.RecordViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToLong


class OcrFragment : Fragment(), BillDetailsAdapter.OnPriceListener, OnYesOrNoListener {
    private var _binding : FragmentOcrBinding? = null
    private val binding : FragmentOcrBinding
        get() = _binding!!
    private val recordViewModel : RecordViewModel by viewModels()
    private lateinit var adapter : BillDetailsAdapter
    private lateinit var pickMedia : ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var cameraLauncher : ActivityResultLauncher<Intent>
    private var cameraImage : Uri? = null
    private val args: OcrFragmentArgs by navArgs()
    private lateinit var data : ReceiptDetail
    private lateinit var currentDate : String
    private lateinit var currency : String
    private lateinit var storeName : String
    private var camera = false
    private var id = -1L
    private var receiptId = -1L
    private var totalPrice = 0.0
    private var ocr = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentDate = args.date
        currency = args.currency
        camera = args.camera
        id = args.id
        receiptId = args.receiptId
        storeName = args.storeName

        Log.d("OcrFragment", "currentDate : $currentDate\ncurrency : $currency\ncamera : $camera\nid : $id\nreceiptId : $receiptId\nstoreName : $storeName")

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
                Log.d("PhotoPicker", "Selected URI: $uri")
                Glide.with(binding.root)
                    .load(uri)
                    .error(R.color.grey)
                    .into(binding.image)
                if (ocr) {
                    lock()
                    recordViewModel.uploadReceipt(requireContext(), uri)
                }else {
                    recordViewModel.getImageUri(uri)
                    binding.save.isEnabled = false
                }

            } else {
                Log.d("PhotoPicker", "No media selected")
                unlock()
            }
        }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (cameraImage != null){
                    lock()
                    Glide.with(binding.root)
                        .load(cameraImage)
                        .error(R.color.grey)
                        .into(binding.image)
                    recordViewModel.uploadReceipt(requireContext(), cameraImage!!)
                }
            }
        }

        recordViewModel.saveCheckLiveData.observe(viewLifecycleOwner){
            if (it){
                close()
            }else{
                Toast.makeText(requireContext(), getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
            }
        }

        recordViewModel.uriLiveData.observe(viewLifecycleOwner){
            if (!ocr)
                binding.save.isEnabled = true
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

        if (receiptId > -1){
            lifecycleScope.launch {
                binding.edit.visibility = View.VISIBLE
                binding.save.text = getString(R.string.close)
                binding.add.visibility = View.GONE
                binding.image.isEnabled = false
                adapter.finishEdit()
                getReceiptDetail()
            }
        }else{
            imageInit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (receiptId > -1){
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            }
        }
    }

    fun edit(){
        adapter.startEdit()
        binding.edit.visibility = View.GONE
        binding.save.text = getString(R.string.save_long)
        binding.add.visibility = View.VISIBLE
        binding.image.isEnabled = true
    }

    fun putImage(){
        if (receiptId > -1){
            YesOrNoDialog("",
                YesOrNoDialog.OCR_CHECK,
                onYesOrNoListener = this).show(childFragmentManager, "YesOrNoDialog")
            return
        }
        gallery()
    }

    private fun gallery(){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun camera(){
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraImage = recordViewModel.createUri(requireContext().contentResolver)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImage)
            cameraLauncher.launch(intent)
        }catch (e : Exception){
            Toast.makeText(requireContext(), getString(R.string.camera_failed), Toast.LENGTH_SHORT).show()
        }
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
        if (receiptId > -1 && !adapter.getEditMode()){
            binding.save.text = getString(R.string.close)
            binding.add.visibility = View.GONE
            binding.image.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun add(){
        adapter.add()
    }

    fun confirm() {
        val editMode = adapter.getEditMode()
        if (receiptId > -1 && !editMode){
            close()
        }else if (receiptId > -1 && editMode){
            update()
        }else{
            save()
        }
    }

    private fun close(){
        val activity = requireActivity() as? BillInputActivity
        activity?.closeAllFragmentsAndFinishActivity()
    }

    private fun update(){
        lifecycleScope.launch {
            try {
                val storeName = binding.title.text.toString()
                if (storeName.isNullOrBlank()) data.storeName = getString(R.string.empty_title) else data.storeName = storeName
                    data.totalPrice = totalPrice
                    data.receiptDetails = adapter.getAllData()
                    Log.d("OcrFragment", "size : ${data.receiptDetails.size}\ndata : ${data.receiptDetails[0].title}\t ${data.receiptDetails[0].count}\t ${data.receiptDetails[0].itemPrice}")
                    recordViewModel.updateReceipt(requireContext(), receiptId, data)

            }catch (e: Exception){
                Toast.makeText(requireContext(), getString(R.string.error_message_simple, getString(
                    R.string.receipt_update
                )), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun save(){
        lifecycleScope.launch {
            try {
                val storeName = binding.title.text.toString()
                if (storeName.isNullOrBlank()) data.storeName = getString(R.string.empty_title) else data.storeName = storeName
                data.totalPrice = totalPrice
                data.receiptDetails = adapter.getAllData()
                recordViewModel.saveReceipt(requireContext(), data)
            }catch (e : Exception){
                ConfirmDialog(listOf(getString(R.string.image))).show(childFragmentManager, "ConfirmDialog")
            }
        }
    }

    private fun imageInit(){
        if (camera){
            camera()
        }else{
            putImage()
        }
    }

    private suspend fun getReceiptDetail(){
        recordViewModel.getReceiptDetail(receiptId).collect{data->
            if (data != null){
                val d = recordViewModel.getReceiptDetail(data, storeName)
                d?.let {
                    this.data = it
                    adapter.update(this.data.receiptDetails)
                    setUI(this.data)
                    Glide.with(binding.root)
                        .load(data.image)
                        .error(R.color.grey)
                        .into(binding.image)
                } ?: {
                    Toast.makeText(requireContext(), getString(R.string.non_have_grant), Toast.LENGTH_SHORT).show()
                    val activity = requireActivity() as? BillInputActivity
                    activity?.closeAllFragmentsAndFinishActivity()
                }
                return@collect
            }
        }
    }

    override fun onTotalPriceListener(total: Double) {
        val formatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(total)
        binding.total.text = getString(R.string.bill_total_price, formatted, currency)
        totalPrice = ((total*100).roundToLong().toDouble()/100)
    }

    override fun onScheduleDeleteListener(position: Int) {

    }

    override fun onKeepCheckListener() {
    }

    override fun onCameraListener(answer: Boolean) {
    }

    override fun onOcrListener(answer: Boolean) {
        ocr = answer
        gallery()
    }

}