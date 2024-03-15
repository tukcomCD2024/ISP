package com.project.how.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.R
import com.project.how.adapter.recyclerview.AiDaysScheduleAdapter
import com.project.how.data_class.AiDaysSchedule
import com.project.how.data_class.AiSchedule
import com.project.how.data_class.DaysSchedule
import com.project.how.data_class.Schedule
import com.project.how.data_class.dto.GetScheduleListResponse
import com.project.how.data_class.dto.ScheduleDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class ScheduleRepository {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time.time
    private val _nearScheduleDayLiveData : MutableLiveData<Long> = MutableLiveData()
    private val _scheduleLiveData : MutableLiveData<Schedule> = MutableLiveData()
    private val _scheduleListLiveData : MutableLiveData<GetScheduleListResponse> = MutableLiveData()
    val nearScheduleDayLiveData : LiveData<Long>
        get() = _nearScheduleDayLiveData
    val scheduleLiveData : LiveData<Schedule>
        get() = _scheduleLiveData
    val scheduleListLiveData : LiveData<GetScheduleListResponse>
        get() = _scheduleListLiveData


    fun getDday() : Flow<Long> = flow {
        this.emit(((nearScheduleDayLiveData.value?.minus(today))?.div((24 * 60 * 60 * 1000)) ?: ERROR) as Long)
    }

    fun getNearScheduleDay(day : Long){
        _nearScheduleDayLiveData.postValue(day)
    }

    suspend fun getSchedule(schedule : Schedule) {
        getTotalCost(schedule).collect{
            schedule.cost = it
            getScheduleAndTotalCost(schedule)
        }
    }

    private fun getScheduleAndTotalCost(schedule : Schedule){
        _scheduleLiveData.postValue(schedule)
    }

    fun getSchedule(aiSchedule : AiSchedule) : Flow<Schedule> = flow{
        this.emit(Schedule(
            aiSchedule.title,
            aiSchedule.country,
            aiSchedule.startDate,
            aiSchedule.endDate,
            0,
            getDailySchedule(aiSchedule.dailySchedule)
        ))
    }

    fun getTotalCost(schedule: Schedule) :Flow<Long> = flow {
        var totalCost : Long = 0
        for (i in schedule.dailySchedule.indices){
            for (j in schedule.dailySchedule[i].indices){
                totalCost += schedule.dailySchedule[i][j].cost
            }
        }
        this.emit(totalCost)
    }

    private fun getDailySchedule(aiDailySchedule : List<List<AiDaysSchedule>>): MutableList<MutableList<DaysSchedule>> {
        val dailySchedule = mutableListOf<MutableList<DaysSchedule>>()
        for(i in aiDailySchedule.indices){
            val oneDaysSchedule = mutableListOf<DaysSchedule>()
            for (j in aiDailySchedule[i].indices){
                oneDaysSchedule.add(
                    DaysSchedule(
                        aiDailySchedule[i][j].type,
                        aiDailySchedule[i][j].todo,
                        aiDailySchedule[i][j].places,
                        null,
                        null,
                        0,
                        false,
                        null
                    )
                )
            }
            dailySchedule.add(oneDaysSchedule)
        }
        return dailySchedule
    }

    suspend fun updateDailySchedule(schedule: Schedule, startDate: String, endDate: String) {
        val std = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE)
        val end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE)
        val diff = ChronoUnit.DAYS.between(std, end)
        Log.d("setDaysTab", "updateDailySchedule\nstd : $std\tend : $end\ndiff : $diff\t schedule.dailySchedule.lastIndex : ${schedule.dailySchedule.size}")
        if (schedule.dailySchedule.lastIndex < diff){
            for(i in 0 until  diff - schedule.dailySchedule.lastIndex){
                Log.d("setDaysTab", "updateDailySchedule schedule.dailySchedule.lastIndex < diff\n schedule.dailySchedule.add(mutableListOf<DaysSchedule>())")
                schedule.dailySchedule.add(mutableListOf())
            }
            Log.d("setDaysTab", "updateDailySchedule schedule.dailySchedule.lastIndex < diff\n after size : ${schedule.dailySchedule.size}")
            getSchedule(schedule)
        }else if(schedule.dailySchedule.lastIndex.toLong() == diff){
            Log.d("setDaysTab", "updateDailySchedule schedule.dailySchedule.lastIndex == diff\n schedule.dailySchedule.add(mutableListOf<DaysSchedule>())")
            Log.d("setDaysTab", "updateDailySchedule schedule.dailySchedule.lastIndex == diff\n after size : ${schedule.dailySchedule.size}")
            getSchedule(schedule)
        }else{
            for (i in 0 until schedule.dailySchedule.lastIndex - diff){
                Log.d("setDaysTab", "updateDailySchedule schedule.dailySchedule.lastIndex > diff\n schedule.dailySchedule.removeLast()")
                schedule.dailySchedule.removeLast()
            }
            Log.d("setDaysTab", "updateDailySchedule schedule.dailySchedule.lastIndex > diff\n after size : ${schedule.dailySchedule.size}")
            getSchedule(schedule)
        }
    }

    fun getScheduleList(scheduleList : GetScheduleListResponse){
        _scheduleListLiveData.postValue(scheduleList)
    }

    fun getDaysSchedule(context: Context, schedule: Schedule, scheduleDetail: ScheduleDetail) = flow<Schedule>{

        for(i in scheduleDetail.dailySchedules.indices){
            val dailySchedule = mutableListOf<DaysSchedule>()
            for (j in scheduleDetail.dailySchedules[i].schedules.indices){
                dailySchedule.add(
                    DaysSchedule(
                        getScheduleType(context, scheduleDetail.dailySchedules[i].schedules[j].type),
                        scheduleDetail.dailySchedules[i].schedules[j].todo,
                        scheduleDetail.dailySchedules[i].schedules[j].place,
                        scheduleDetail.dailySchedules[i].schedules[j].latitude,
                        scheduleDetail.dailySchedules[i].schedules[j].longitude,
                        scheduleDetail.dailySchedules[i].schedules[j].budget,
                        false,
                        null
                    )
                )
            }
            schedule.dailySchedule.add(dailySchedule)
            emit(schedule)
        }
    }

    private fun getScheduleType(context: Context, type : String) : Int {
        when(type){
            context.getString(R.string.airplane_string) -> {return AiDaysScheduleAdapter.AIRPLANE}
            context.getString(R.string.hotel_string) -> {return AiDaysScheduleAdapter.HOTEL}
            context.getString(R.string.place_string) -> {return AiDaysScheduleAdapter.PLACE}
        }
        return AiDaysScheduleAdapter.PLACE
    }

    companion object{
        const val ERROR = -1
    }
}