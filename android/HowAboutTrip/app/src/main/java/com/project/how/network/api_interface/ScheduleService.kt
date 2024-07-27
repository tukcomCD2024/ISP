package com.project.how.network.api_interface

import com.project.how.data_class.dto.schedule.CreateScheduleListRequest
import com.project.how.data_class.dto.schedule.CreateScheduleListResponse
import com.project.how.data_class.dto.schedule.CreateScheduleResponse
import com.project.how.data_class.dto.country.GetCountryInfoRequest
import com.project.how.data_class.dto.country.GetCountryLocationResponse
import com.project.how.data_class.dto.schedule.GetFastestSchedulesResponse
import com.project.how.data_class.dto.schedule.GetLatestSchedulesResponse
import com.project.how.data_class.dto.schedule.GetScheduleListResponse
import com.project.how.data_class.dto.schedule.ScheduleDetail
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleService {
    @POST("gpt/schedules")
    fun createSchedule(
        @Body createCondition : CreateScheduleListRequest
    ) : Call<CreateScheduleResponse>

    @POST("gpt/schedules")
    fun createScheduleList(
        @Body createCondition: CreateScheduleListRequest
    ) : Call<CreateScheduleListResponse>

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
        @Body country : GetCountryInfoRequest
    ) : Call<GetCountryLocationResponse>

    @GET("schedules/dday")
    fun getFastestSchedule(
        @Header("Authorization") accessToken : String
    ) : Call<GetFastestSchedulesResponse>

    @GET("schedules/latest")
    fun getLatestSchedules(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Long
    ) : Call<GetLatestSchedulesResponse>
}