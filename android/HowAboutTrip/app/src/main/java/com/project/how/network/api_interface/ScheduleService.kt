package com.project.how.network.api_interface

import com.project.how.data_class.dto.CreateScheduleRequest
import com.project.how.data_class.dto.CreateScheduleRespone
import com.project.how.data_class.dto.SaveScheduleRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ScheduleService {
    @POST("schedules/schedule")
    fun createSchedule(
        @Body createCondition : CreateScheduleRequest
    ) : Call<CreateScheduleRespone>

    @POST("schedules/save")
    fun saveSchedule(
        @Header("Authorization") accessToken : String,
        @Body saveScheduleRequest: SaveScheduleRequest
    ) : Call<String>
}