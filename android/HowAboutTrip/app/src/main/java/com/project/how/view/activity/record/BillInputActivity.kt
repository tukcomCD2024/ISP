package com.project.how.view.activity.record

import android.os.Bundle
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
    private lateinit var dateRange : String
    private lateinit var currency : String
    private var id = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bill_input)
        binding.input = this
        binding.lifecycleOwner = this

        currentDate = intent.getStringExtra(getString(R.string.current_date)) ?: ""
        dateRange = intent.getStringExtra(getString(R.string.date_range)) ?: ""
        currency = intent.getStringExtra(getString(R.string.currency)) ?: ""
        val id = intent.getLongExtra(getString(R.string.server_calendar_id), -1)
        this.id = id.toLong()


        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.navigateUp()){
                    isEnabled = false
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
        finish()
    }

    fun getCurrentDate() = currentDate

    fun getDateRange() = dateRange

    fun getCurrency() = currency

    fun getId() = id
}
