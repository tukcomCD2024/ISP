package com.project.how.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.ActivityMainBinding
import com.project.how.view.activity.ai.AddAICalendarActivity
import com.project.how.view.fragment.main.CalendarFragment
import com.project.how.view.fragment.main.MypageFragment
import com.project.how.view.fragment.main.RecordFragment
import com.project.how.view.fragment.main.TicketFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this
        binding.lifecycleOwner = this

        val menu = intent.getIntExtra(getString(R.string.menu_intent), 2)

        if (!allPermissionsGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE)
        }

        supportFragmentManager.beginTransaction().add(R.id.fragment, CalendarFragment()).commitAllowingStateLoss()
        binding.menu.menu.getItem(menu).isEnabled = false
        binding.menu.selectedItemId = R.id.menu_calendar
        binding.menu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_ticket->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, TicketFragment())
                        .commitAllowingStateLoss()
                }
                R.id.menu_calendar->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, CalendarFragment())
                        .commitAllowingStateLoss()
                }
                R.id.menu_picture->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, RecordFragment())
                        .commitAllowingStateLoss()
                }
                R.id.menu_mypage->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, MypageFragment())
                        .commitAllowingStateLoss()
                }
            }
            true
        }
    }

    fun moveAddAICalendar(){
        startActivity(Intent(this, AddAICalendarActivity::class.java))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object{
        const val TICKET = 1
        const val CALENDAR = 2
        const val PICTURE = 3
        const val MY_PAGE = 4

        const val REQUEST_CODE = 10
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}