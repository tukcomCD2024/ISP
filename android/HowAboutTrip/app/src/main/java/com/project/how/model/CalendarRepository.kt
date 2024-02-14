package com.project.how.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class CalendarRepository {
    private val nowDate: LocalDate = LocalDate.now()
    private val _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate : LiveData<LocalDate>
        get() = _selectedDate
    private lateinit var monthOfFirstDate : LocalDate
    private lateinit var sd : LocalDate
    private var monthOfFirstDayOfWeek = EMPTY
    private var lastDay = 0

    private fun getMonthInform(fdw : Int) : Flow<List<Int>> = flow {
        val monthInfo = mutableListOf<Int>()
        lastDay = sd.lengthOfMonth()
        var week = fdw
        for (i in 0 until fdw)
            monthInfo.add(EMPTY)
        for(i in 1..lastDay){
            monthInfo.add(week)
            week = (week+1) % 7
        }
        this.emit(monthInfo)
    }

    private fun getMonthOfFirstDayOfWeekChangeInt() : Int {
        when(monthOfFirstDate.dayOfWeek.toString()){
            "MONDAY" -> return 1
            "TUESDAY" -> return 2
            "WEDNESDAY" -> return 3
            "THURSDAY" -> return 4
            "FRIDAY" -> return 5
            "SATURDAY" -> return 6
            "SUNDAY" -> return 0
            else -> {
                Log.e("getMonthOfFirstDayOfWeek", "monthOfFirstDate.dayOfWeek.toString() go to else case\n${monthOfFirstDate.dayOfWeek}")
                return 99}
        }
    }

    private fun getFirstDate(): Flow<List<Int>> {
        monthOfFirstDate = sd.withDayOfMonth(1)!!
        monthOfFirstDayOfWeek = getMonthOfFirstDayOfWeekChangeInt()
        return getMonthInform(monthOfFirstDayOfWeek)
    }

    fun plusSelectedDate(): Flow<List<Int>> {
        sd = sd.plusMonths(1)
        _selectedDate.postValue(sd)
        return getFirstDate()
    }

    fun minusSelectedDate(): Flow<List<Int>> {
        sd = sd.minusMonths(1)
        _selectedDate.postValue(sd)
        return getFirstDate()
    }

    fun init() : Flow<List<Int>>{
        sd = nowDate
        _selectedDate.postValue(nowDate)
        return getFirstDate()
    }

    companion object {
        private const val EMPTY = 99
    }

}