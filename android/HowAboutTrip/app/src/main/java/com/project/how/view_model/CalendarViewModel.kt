package com.project.how.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.model.CalendarRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class CalendarViewModel : ViewModel() {
    private var calendarRepository : CalendarRepository = CalendarRepository()
    private val _selectedDate = calendarRepository.selectedDate
    val selectedDate : LiveData<LocalDate>
        get() = _selectedDate

    fun init() : Flow<List<Int>> = calendarRepository.init()

    fun plusSelectedDate() : Flow<List<Int>> = calendarRepository.plusSelectedDate()

    fun minusSelectedDate() : Flow<List<Int>> = calendarRepository.minusSelectedDate()
}