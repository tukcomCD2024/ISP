package com.project.how.view.activity.record

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.record.BillListAdapter
import com.project.how.data_class.dto.recode.receipt.ReceiptListItem
import com.project.how.databinding.ActivityBillListBinding
import com.project.how.view_model.RecordViewModel

class BillListActivity : AppCompatActivity(), BillListAdapter.OnItemClickListener {
    private lateinit var binding : ActivityBillListBinding
    private lateinit var adapter : BillListAdapter
    private val recordViewModel : RecordViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_list)
        binding.bill = this
        binding.lifecycleOwner = this
        adapter = BillListAdapter(listOf<ReceiptListItem>(), this, this)
        binding.billList.adapter = adapter

        recordViewModel.receiptSimpleListLiveData.observe(this){
            if (it[0].scheduleName == null){
                Toast.makeText(this, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                return@observe
            }
            adapter.update(it)
        }

        recordViewModel.getScheduleListWithReceipt()

    }

    override fun onItemClickListener(id: Long) {
        val intent = Intent(this, BillActivity::class.java)
        intent.putExtra(getString(R.string.server_calendar_id), id)
        startActivity(intent)
        finish()
    }

    override fun onDeleteButtonClickListener(id: Long, position: Int) {
        //adapter.delete(position)
    }
}