package com.project.how

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.project.how.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var text = "Hello World!"
    var isClicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.test = this

        binding.hideButton.setOnClickListener {
            isClicked = !isClicked
            binding.invalidateAll()
        }
    }
}