package com.project.how.network.api_interface

import com.project.how.data_class.dto.CreateScheduleRequest
import com.project.how.data_class.dto.CreateScheduleRespone
import com.project.how.data_class.dto.GetScheduleListResponse
import com.project.how.data_class.dto.ScheduleDetail
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ScheduleService {
    @POST("schedules/schedule")
    fun createSchedule(
        @Body createCondition : CreateScheduleRequest
    ) : Call<CreateScheduleRespone>

    @POST("schedules/save")
    fun saveSchedule(
        @Header("Authorization") accessToken : String,
        @Body saveScheduleRequest: ScheduleDetail
    ) : Call<String>

    @GET("schedules/list")
    fun getScheduleList(
        @Header("Authorization") accessToken : String
    ) : Call<GetScheduleListResponse>

    @GET("schedules/detail/{scheduleId}")
    fun getScheduleDetail(
        @Header("Authorization") accessToken : String,
        @Path("scheduleId", encoded = true) scheduleId : Int
    ) : Call<ScheduleDetail>

    @DELETE("schedules/{scheduleId}")
    fun deleteSchedule(
        @Header("Authorization") accessToken : String,
        @Path("scheduleId", encoded = true) scheduleId : Int
    ) : Call<String>

    @PUT("schedules/update/{scheduleId}")
    fun updateSchedule(
        @Header("Authorization") accessToken : String,
        @Body updateScheduleRequest: ScheduleDetail
    ) : Call<ScheduleDetail>
}