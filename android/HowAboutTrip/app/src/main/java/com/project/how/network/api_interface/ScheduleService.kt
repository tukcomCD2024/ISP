package com.project.how.network.api_interface

import com.project.how.data_class.dto.CreateScheduleRequest
import com.project.how.data_class.dto.CreateScheduleRespone
import com.project.how.data_class.dto.SaveScheduleRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ScheduleService {
    @POST("schedule/schedule")
    fun createSchedule(
        @Body createCondition : CreateScheduleRequest
    ) : Call<CreateScheduleRespone>

    @POST("schedule/save")
    fun saveSchedule(
        @Body saveScheduleRequest: SaveScheduleRequest
    ) : Call<String>
}