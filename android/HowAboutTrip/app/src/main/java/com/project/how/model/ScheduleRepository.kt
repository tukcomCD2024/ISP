package com.project.how.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.how.R
import com.project.how.adapter.recyclerview.schedule.AiDaysScheduleAdapter
import com.project.how.data_class.dto.schedule.CheckList
import com.project.how.data_class.dto.schedule.GetFastestSchedulesResponse
import com.project.how.data_class.dto.schedule.GetLatestSchedulesResponse
import com.project.how.data_class.recyclerview.schedule.AiDaysSchedule
import com.project.how.data_class.recyclerview.schedule.AiSchedule
import com.project.how.data_class.recyclerview.schedule.DaysSchedule
import com.project.how.data_class.recyclerview.schedule.Schedule
import com.project.how.data_class.dto.schedule.GetScheduleListResponse
import com.project.how.data_class.dto.schedule.ScheduleDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ScheduleRepository {
    private val _nearScheduleDayLiveData : MutableLiveData<GetFastestSchedulesResponse> = MutableLiveData()
    private val _scheduleLiveData : MutableLiveData<Schedule> = MutableLiveData()
    private val _scheduleListLiveData : MutableLiveData<GetScheduleListResponse> = MutableLiveData()
    private val _latestScheduleLiveData : MutableLiveData<GetLatestSchedulesResponse> = MutableLiveData()
    private val _checkList : MutableLiveData<CheckList> = MutableLiveData()
    val nearScheduleDayLiveData : LiveData<GetFastestSchedulesResponse>
        get() = _nearScheduleDayLiveData
    val scheduleLiveData : LiveData<Schedule>
        get() = _scheduleLiveData
    val scheduleListLiveData : LiveData<GetScheduleListResponse>
        get() = _scheduleListLiveData
    val latestScheduleLiveData : LiveData<GetLatestSchedulesResponse>
        get() = _latestScheduleLiveData
    val checkList : LiveData<CheckList>
        get() = _checkList

    fun getCheckList(data : CheckList){
        _checkList.postValue(data)
    }

    fun getNearScheduleDay(data : GetFastestSchedulesResponse){
        _nearScheduleDayLiveData.postValue(data)
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
        this.emit(
            Schedule(
            aiSchedule.title,
            aiSchedule.country,
            aiSchedule.startDate,
            aiSchedule.endDate,
            aiSchedule.budget,
            getDailySchedule(aiSchedule.dailySchedule)
        )
        )
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

    fun getLatestSchedule(data : GetLatestSchedulesResponse){
        _latestScheduleLiveData.postValue(data)
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
                        aiDailySchedule[i][j].lat,
                        aiDailySchedule[i][j].lng,
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
        val std = LocalDate.parse(scheduleDetail.startDate, DateTimeFormatter.ISO_DATE)
        val end = LocalDate.parse(scheduleDetail.endDate, DateTimeFormatter.ISO_DATE)
        val diff = ChronoUnit.DAYS.between(std, end)
        var cnt = 0

        for(i in 0..diff.toInt()){
            val dailySchedule = mutableListOf<DaysSchedule>()
            val currentDate = std.plusDays(i.toLong())
            val cd = currentDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
            Log.d("getDaysSchedule", "${scheduleDetail.scheduleName} \t ${scheduleDetail.startDate} \t ${scheduleDetail.endDate}\n${i+1} 일차\t cnt : $cnt\tdailySchedule.lastIndex : ${scheduleDetail.dailySchedules.lastIndex}\n이번 날짜 : $cd 대기중인 일정 날짜 : ${scheduleDetail.dailySchedules[cnt].date}")
            if (cd == scheduleDetail.dailySchedules[cnt].date){
                if(scheduleDetail.dailySchedules[cnt].schedules.isNotEmpty()){
                    for (j in 0 until scheduleDetail.dailySchedules[cnt].schedules.size){
                        dailySchedule.add(
                            DaysSchedule(
                                getScheduleType(context, scheduleDetail.dailySchedules[cnt].schedules[j].type),
                                scheduleDetail.dailySchedules[cnt].schedules[j].todo,
                                scheduleDetail.dailySchedules[cnt].schedules[j].place,
                                scheduleDetail.dailySchedules[cnt].schedules[j].latitude,
                                scheduleDetail.dailySchedules[cnt].schedules[j].longitude,
                                scheduleDetail.dailySchedules[cnt].schedules[j].budget,
                                false,
                                null
                            )
                        )
                    }
                }
                if (cnt != scheduleDetail.dailySchedules.lastIndex){
                    cnt++
                }
            }
            schedule.dailySchedule.add(dailySchedule)
        }
        emit(schedule)
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