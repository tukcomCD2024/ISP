package com.project.how.view.activity.record

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.project.how.R
import com.project.how.databinding.ActivityBillInputBinding


class BillInputActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBillInputBinding
    private lateinit var navController: NavController
    private lateinit var currentDate : String
    private lateinit var currency : String
    private lateinit var storeName : String
    private var id = -1L
    private var receiptId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_input)
        binding.input = this
        binding.lifecycleOwner = this

        currentDate = intent.getStringExtra(getString(R.string.current_date)) ?: ""
        currency = intent.getStringExtra(getString(R.string.currency)) ?: ""
        storeName = intent.getStringExtra(getString(R.string.store_name)) ?: ""
        val id = intent.getLongExtra(getString(R.string.server_calendar_id), -1)
        val receiptId = intent.getLongExtra(getString(R.string.receipt_id), -1)
        this.id = id.toLong()
        this.receiptId = receiptId.toLong()
        Log.d("BillInputActivity", "receiptId : $receiptId\nthis.receiptId : ${this.receiptId}")


        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.navigateUp()){
                    isEnabled = false
                    val intent = Intent(this@BillInputActivity, BillActivity::class.java)
                    intent.putExtra(getString(R.string.server_calendar_id), id)
                    startActivity(intent)
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        navController.addOnDestinationChangedListener { _, _, _ ->
            val showUpButton = false
            supportActionBar?.setDisplayHomeAsUpEnabled(showUpButton)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun closeAllFragmentsAndFinishActivity() {
        navController.popBackStack(navController.graph.id, true)
        onBackPressedDispatcher.onBackPressed()
    }

    fun getReceiptId() = receiptId

    fun getCurrentDate() = currentDate

    fun getCurrency() = currency

    fun getId() = id

    fun getStoreName() = storeName
}
