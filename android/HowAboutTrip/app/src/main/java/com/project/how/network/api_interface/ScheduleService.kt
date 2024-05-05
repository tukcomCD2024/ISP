package com.project.how.network.api_interface

import com.project.how.data_class.dto.CreateScheduleRequest
import com.project.how.data_class.dto.CreateScheduleResponse
import com.project.how.data_class.dto.GetCountryLocationRequest
import com.project.how.data_class.dto.GetCountryLocationResponse
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
    @POST("gpt/schedules")
    fun createSchedule(
        @Body createCondition : CreateScheduleRequest
    ) : Call<CreateScheduleResponse>

    @POST("schedules")
    fun saveSchedule(
        @Header("Authorization") accessToken : String,
        @Body saveScheduleRequest: ScheduleDetail
    ) : Call<String>

    @GET("schedules")
    fun getScheduleList(
        @Header("Authorization") accessToken : String
    ) : Call<GetScheduleListResponse>

    @GET("schedules/details/{scheduleId}")
    fun getScheduleDetail(
        @Header("Authorization") accessToken : String,
        @Path("scheduleId", encoded = true) scheduleId : Long
    ) : Call<ScheduleDetail>

    @DELETE("schedules/{scheduleId}")
    fun deleteSchedule(
        @Header("Authorization") accessToken : String,
        @Path("scheduleId", encoded = true) scheduleId : Long
    ) : Call<String>

    @PUT("schedules/{scheduleId}")
    fun updateSchedule(
        @Header("Authorization") accessToken : String,
        @Path("scheduleId", encoded = true) scheduleId: Long,
        @Body updateScheduleRequest: ScheduleDetail
    ) : Call<ScheduleDetail>

    @POST("countries/locations")
    fun getCountryLocation(
        @Body country : GetCountryLocationRequest
    ) : Call<GetCountryLocationResponse>
}