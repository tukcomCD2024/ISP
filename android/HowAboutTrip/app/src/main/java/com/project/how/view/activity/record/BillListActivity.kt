package com.project.how.view.activity.record

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.record.BillListAdapter
import com.project.how.data_class.recyclerview.record.Bill
import com.project.how.databinding.ActivityBillListBinding

class BillListActivity : AppCompatActivity(), BillListAdapter.OnItemClickListener {
    private lateinit var binding : ActivityBillListBinding
    private lateinit var adapter : BillListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_list)
        binding.bill = this
        binding.lifecycleOwner = this
        adapter = BillListAdapter(listOf<Bill>(
            Bill(
                1,
                BuildConfig.TEMPORARY_IMAGE_URL,
                "Temporary1",
                "2024.01.20 - 2024.01.24",
                0,
                3
            )
        ),
            this, this)
        binding.billList.adapter = adapter
    }

    override fun onItemClickListener(id: Long) {
        val intent = Intent(this, BillActivity::class.java)
        intent.putExtra(getString(R.string.server_calendar_id), 32L)
        startActivity(intent)
    }

    override fun onDeleteButtonClickListener(id: Long, position: Int) {
        adapter.delete(position)
    }
}