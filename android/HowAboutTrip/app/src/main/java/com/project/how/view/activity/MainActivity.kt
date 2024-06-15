package com.project.how.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.project.how.R
import com.project.how.databinding.ActivityMainBinding
import com.project.how.view.activity.ai.AddAICalendarActivity
import com.project.how.view.fragment.main.CalendarFragment
import com.project.how.view.fragment.main.MypageFragment
import com.project.how.view.fragment.main.PictureFragment
import com.project.how.view.fragment.main.TicketFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this
        binding.lifecycleOwner = this

        val menu = intent.getIntExtra(getString(R.string.menu_intent), 2)

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
                    supportFragmentManager.beginTransaction().replace(R.id.fragment, PictureFragment())
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
    }companion object{
        const val TICKET = 1
        const val CALENDAR = 2
        const val PICTURE = 3
        const val MY_PAGE = 4
    }
}