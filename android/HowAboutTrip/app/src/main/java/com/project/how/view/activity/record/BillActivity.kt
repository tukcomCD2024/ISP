package com.project.how.view.activity.record

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.project.how.R
import com.project.how.adapter.recyclerview.record.BillDaysAdapter
import com.project.how.data_class.dto.recode.receipt.GetReceiptListResponse
import com.project.how.data_class.dto.recode.receipt.ReceiptList
import com.project.how.databinding.ActivityBillBinding
import com.project.how.view.dialog.AiScheduleDialog
import com.project.how.view.dp.DpPxChanger
import com.project.how.view_model.RecordViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


class BillActivity : AppCompatActivity(), BillDaysAdapter.OnItemClickListener {
    private lateinit var binding : ActivityBillBinding
    private val recordViewModel : RecordViewModel by viewModels()
    private lateinit var adapter : BillDaysAdapter
    private var id = 0L
    private var currentTab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill)
        binding.bill = this
        binding.lifecycleOwner = this
        adapter = BillDaysAdapter(mutableListOf<ReceiptList>(), this, this)
        binding.dayBills.adapter = adapter

        recordViewModel.currentReceiptList.observe(this){data->
            lifecycleScope.launch {
                val currentDate = recordViewModel.getCurrentDate(data.startDate, currentTab)
                adapter.update(data.receiptList.filter { it.purchaseDate == currentDate }.toMutableList())
                setUI(data)
            }
        }

        init()

    }

    fun add(){
        val intent = Intent(this, BillInputActivity::class.java)
        intent.putExtra(getString(R.string.server_calendar_id), id)
        intent.putExtra(getString(R.string.current_tab), currentTab)
        startActivity(intent)
    }

    private fun init(){
        lifecycleScope.launch {
            val id = intent.getLongExtra(getString(R.string.server_calendar_id), -1)
            currentTab = intent.getIntExtra(getString(R.string.current_tab), 0)
            this@BillActivity.id = id.toLong()

            recordViewModel.getReceiptList(this@BillActivity.id)
        }
    }

    private fun setUI(data : GetReceiptListResponse){
        binding.title.text = data.scheduleName
        binding.date.text = "${data.startDate} - ${data.endDate}"
        binding.cost.text = NumberFormat.getNumberInstance(Locale.getDefault()).format(data.totalReceiptsPrice)
        binding.costUnit.text = " ${data.currencyName}"
        binding.daysTitle.text = getString(R.string.days_title, (currentTab + 1).toString(), recordViewModel.getDaysTitle(data.startDate, currentTab))

        setDaysTab(data.startDate, data.endDate)
        setDaysTabItemMargin()

        binding.daysTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                lifecycleScope.launch {
                    currentTab = binding.daysTab.selectedTabPosition
                    binding.daysTitle.text = getString(R.string.days_title,
                        (currentTab + 1).toString(),
                        recordViewModel.getDaysTitle(data.startDate, currentTab))
                    val currentDate = recordViewModel.getCurrentDate(data.startDate, currentTab)
                    adapter.update(data.receiptList.filter { it.purchaseDate == currentDate }.toMutableList())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun setDaysTab(startDate : String, endDate : String){
        val sd = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE)
        val ed = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE)
        val daysBetween = ChronoUnit.DAYS.between(sd, ed)

        for(i in 0..daysBetween){
            val tab = binding.daysTab.newTab().setText("${i+1}일차")
            binding.daysTab.addTab(tab)
        }
    }

    private fun setDaysTabItemMargin(){
        val tabs = binding.daysTab.getChildAt(0) as ViewGroup
        for(i in 0 until tabs.childCount){
            val tab = tabs.getChildAt(i)
            val lp = tab.layoutParams as LinearLayout.LayoutParams
            val dpPxChanger = DpPxChanger()
            lp.marginEnd = dpPxChanger.dpToPx(this, AiScheduleDialog.TAB_ITEM_MARGIN)
            lp.width = dpPxChanger.dpToPx(this, AiScheduleDialog.TAB_ITEM_WIDTH)
            lp.height = dpPxChanger.dpToPx(this, AiScheduleDialog.TAB_ITEM_HEIGHT)
            tab.layoutParams = lp
        }
        binding.daysTab.requestLayout()
    }

    override fun onItemClickListener(data: ReceiptList, position: Int) {

    }

    override fun onMoreMenuDateChangeClickListener(data: ReceiptList, position: Int) {
    }

    override fun onMoreMenuOrderChangeClickListener(position: Int) {
    }

    override fun onMoreMenuDeleteClickListener(position: Int) {
    }

    override fun onMoreMenuEditClickListener(data: ReceiptList, position: Int) {
    }
}