package com.project.how.view_model

import androidx.lifecycle.ViewModel
import com.project.how.model.BookingRepository

class BookingViewModel : ViewModel() {
    private val bookingRepository = BookingRepository()
}