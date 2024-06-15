package com.project.how.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.model.CalendarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {
    private var calendarRepository : CalendarRepository = CalendarRepository()
    private val _selectedDate = calendarRepository.selectedDate
    val selectedDate : LiveData<LocalDate>
        get() = _selectedDate

    fun init() : Flow<List<Int>> = calendarRepository.init()

    fun plusSelectedDate() : Flow<List<Int>> = calendarRepository.plusSelectedDate()

    fun minusSelectedDate() : Flow<List<Int>> = calendarRepository.minusSelectedDate()

    fun getDateList(startDate : String, lastIndex: Int) : Flow<List<String>> = flow {
        val result = mutableListOf<String>()
        result.add(startDate)
        var sd = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        for(i in 1 ..lastIndex){
            sd = sd.plusDays(1)
            result.add(sd.format(formatter))
        }
        emit(result)
    }
}