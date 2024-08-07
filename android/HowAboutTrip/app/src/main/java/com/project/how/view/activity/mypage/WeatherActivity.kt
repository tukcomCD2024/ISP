package com.project.how.view.activity.mypage

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.project.how.BuildConfig
import com.project.how.R
import com.project.how.adapter.recyclerview.weather.WeekWeatherAdapter
import com.project.how.databinding.ActivityWeatherBinding
import com.project.how.interface_af.interface_ff.OnAirportListener
import com.project.how.view.dialog.bottom_sheet_dialog.AirportBottomSheetDialog
import com.project.how.view_model.CalendarViewModel
import com.project.how.view_model.CountryViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity(), OnAirportListener {
    private lateinit var binding : ActivityWeatherBinding
    private val viewModel : CountryViewModel by viewModels()
    private val calendarViewModel : CalendarViewModel by viewModels()
    private lateinit var adapter : WeekWeatherAdapter
    private lateinit var country : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        binding.weather = this
        binding.lifecycleOwner = this
        adapter = WeekWeatherAdapter(listOf())
        binding.week.adapter = adapter

        country = getString(R.string.inchon)
        update(country)

        viewModel.currentWeatherLiveData.observe(this){
            val temp = it.temp.toDouble().roundToInt().toString()
            val minTemp = it.tempMin.toDouble().roundToInt().toString()
            val maxTemp = it.tempMax.toDouble().roundToInt().toString()
            binding.main.text = viewModel.changeMainToKorean(it.main)
            binding.temp.text = getString(R.string.temp, temp)
            binding.country.text = country
            binding.dateTime.text = calendarViewModel.getWeatherDateTime(it.localTime)
            binding.minAndMaxTemps.text = getString(R.string.min_and_max_temps, minTemp, maxTemp)
            binding.description.text = it.description
            Glide.with(binding.root)
                .load(it.iconUrl)
                .error(BuildConfig.ERROR_IMAGE_URL)
                .into(binding.image)
        }

        viewModel.weeklyWeatherLiveData.observe(this){
            adapter.update(it)
        }


    }

    fun update(country : String){
        lifecycleScope.launch {
            viewModel.getCurrentWeather(country).collect{check->
                if (!check){
                    Toast.makeText(this@WeatherActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                    binding.main.text = ""
                    binding.temp.text = getString(R.string.error)
                    binding.country.text = getString(R.string.error)
                    binding.dateTime.text = getString(R.string.error)
                    binding.description.text = ""
                    binding.minAndMaxTemps.text = ""
                }
            }
        }
        lifecycleScope.launch {
            viewModel.getWeeklyWeathers(country).collect{check->
                if (!check){
                    Toast.makeText(this@WeatherActivity, getString(R.string.server_network_error), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showGlobal(){
        val global = AirportBottomSheetDialog(0, this)
        global.show(supportFragmentManager, "AirportBottomSheetDialog")
    }

    override fun onAirportListener(type: Int, airport: String) {
        country = airport
        update(country)
    }
}