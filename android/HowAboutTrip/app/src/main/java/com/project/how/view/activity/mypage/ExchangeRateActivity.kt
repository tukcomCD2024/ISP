package com.project.how.view.activity.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.project.how.R
import com.project.how.data_class.TargetExchangeRate
import com.project.how.databinding.ActivityEactivityBinding
import com.project.how.interface_af.OnDesListener
import com.project.how.view.dialog.bottom_sheet_dialog.ExchangeRateBottomSheetDialog
import com.project.how.view_model.CountryViewModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class ExchangeRateActivity : AppCompatActivity(), OnDesListener {
    private lateinit var binding: ActivityEactivityBinding
    private val viewModel: CountryViewModel by viewModels()
    private var target: TargetExchangeRate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_eactivity)
        binding.exchange = this
        binding.lifecycleOwner = this

        lifecycleScope.launch {
            MobileAds.initialize(this@ExchangeRateActivity)
            val adRequest = AdRequest.Builder().build()
            binding.adView.loadAd(adRequest)
        }

        binding.targetEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.targetEdit.setSelection(binding.targetEdit.text.toString().length)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    val input = binding.targetEdit.text.toString().toDouble()
                    val values = viewModel.getCurrency(
                        input,
                        target!!.targetUnitWonExchangeRate,
                        target!!.targetUnitDollarExchangeRate
                    )
                    val wonValue = values[0]
                    val dollarValue = values[1]

                    binding.wonResult.text = wonValue
                    binding.dollarResult.text = dollarValue
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getTarget(getString(R.string.america))
    }

    private fun getTarget(country: String) {
        lifecycleScope.launch {
            target = viewModel.getTargetExchangeRate(country)
            if (target == null) {
                Toast.makeText(
                    this@ExchangeRateActivity,
                    getString(R.string.server_network_error),
                    Toast.LENGTH_SHORT
                ).show()
                binding.targetExchangeRate.text = getString(R.string.error)
                binding.won.text = ""
                binding.dollar.text = ""
                binding.targetUnit.text = ""
            } else {
                val values = viewModel.getCurrency(
                    target!!.targetUnitStandard,
                    target!!.targetUnitWonExchangeRate,
                    target!!.targetUnitDollarExchangeRate
                )

                binding.targetExchangeRate.text = getString(
                    R.string.target_exchange_rate,
                    target!!.targetCountry,
                    target!!.targetUnitStandard.toString(),
                    target!!.targetUnit
                )
                binding.won.text = getString(R.string.won, values[0])
                binding.dollar.text = getString(R.string.dollar, values[1], "USD")
                binding.targetUnit.text = getString(R.string.target_unit, target!!.targetUnit)
                binding.targetEdit.setText(target!!.targetUnitStandard.toString())

            }
        }
    }

    fun showGlobal() {
        val global = ExchangeRateBottomSheetDialog(
            target?.targetCountry ?: getString(R.string.america),
            this
        )
        global.show(supportFragmentManager, "ExchangeRateBottomSheetDialog")
    }

    override fun onDesListener(des: String) {
        getTarget(des)
    }
}
