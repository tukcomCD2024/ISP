package com.project.how.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.project.how.model.CalendarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {
    private var calendarRepository : CalendarRepository = CalendarRepository()
    private val _selectedDate = calendarRepository.selectedDate
    val selectedDate : LiveData<LocalDate>
        get() = _selectedDate

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
            Log.d("getDateList", "${sd.format(formatter)}")
        }
        emit(result)
    }

    fun getWeatherDateTime(dateTime : String) : String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("MM월 dd일 HH:mm")

        val localDateTime = LocalDateTime.parse(dateTime, inputFormatter)
        return localDateTime.format(outputFormatter)
    }
}