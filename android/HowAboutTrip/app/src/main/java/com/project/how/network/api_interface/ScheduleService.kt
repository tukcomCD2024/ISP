package com.project.how.network.api_interface

import com.project.how.data_class.dto.schedule.CreateScheduleListRequest
import com.project.how.data_class.dto.schedule.CreateScheduleListResponse
import com.project.how.data_class.dto.schedule.AddCheckListsRequest
import com.project.how.data_class.dto.schedule.CheckList
import com.project.how.data_class.dto.schedule.GetFastestSchedulesResponse
import com.project.how.data_class.dto.schedule.GetLatestSchedulesResponse
import com.project.how.data_class.dto.schedule.GetScheduleListResponse
import com.project.how.data_class.dto.schedule.ScheduleDetail
import com.project.how.data_class.dto.schedule.ScheduleDetailToServer
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleService {
    @POST("gpt/schedules")
    fun createScheduleList(
        @Body createCondition: CreateScheduleListRequest
    ) : Call<CreateScheduleListResponse>

    @POST("schedules")
    fun saveSchedule(
        @Body saveScheduleRequest: ScheduleDetailToServer
    ) : Call<String>

    @GET("schedules")
    fun getScheduleList(): Call<GetScheduleListResponse>

    @GET("schedules/details/{scheduleId}")
    fun getScheduleDetail(
        @Path("scheduleId", encoded = true) scheduleId: Long
    ) : Call<ScheduleDetail>

    @DELETE("schedules/{scheduleId}")
    fun deleteSchedule(
        @Path("scheduleId", encoded = true) scheduleId: Long
    ) : Call<String>

    @PUT("schedules/{scheduleId}")
    fun updateSchedule(
        @Path("scheduleId", encoded = true) scheduleId: Long,
        @Body updateScheduleRequest: ScheduleDetailToServer
    ) : Call<ScheduleDetailToServer>

    @GET("schedules/dday")
    fun getFastestSchedule(): Call<GetFastestSchedulesResponse>

    @GET("schedules/latest")
    fun getLatestSchedules(
        @Query("limit") limit: Long
    ) : Call<GetLatestSchedulesResponse>

    @POST("schedules/{scheduleId}/checklists")
    fun addCheckLists(
        @Path("scheduleId", encoded = true) scheduleId: Long,
        @Body newCheckList: AddCheckListsRequest
    ) : Call<CheckList>

    @GET("schedules/{scheduleId}/checklists")
    fun getCheckLists(
        @Path("scheduleId", encoded = true) scheduleId: Long
    ) : Call<CheckList>

    @DELETE("schedules/{scheduleId}/checklists/{checkListId}")
    fun deleteCheckLists(
        @Path("scheduleId", encoded = true) scheduleId: Long,
        @Path("checkListId", encoded = true) checkListId: Long
    ) : Call<CheckList>

    @PUT("schedules/{scheduleId}/checklists")
    fun updateCheckLists(
        @Path("scheduleId", encoded = true) scheduleId: Long,
        @Body updatedChecklist : CheckList
    ) : Call<CheckList>
}