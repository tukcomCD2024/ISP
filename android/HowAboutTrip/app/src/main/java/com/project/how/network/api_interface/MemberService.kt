package com.project.how.network.api_interface

import com.project.how.data_class.dto.EmptyResponse
import com.project.how.data_class.dto.member.LoginRequest
import com.project.how.data_class.dto.member.SignUpRequest
import com.project.how.data_class.dto.member.AuthRecreateRequest
import com.project.how.data_class.dto.member.GetInfoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface MemberService {
    @Headers("No-Authorization: true")
    @POST("members/login")
    fun login(
        @Body login: LoginRequest
    ) : Call<String>

    @PUT("members/signUp")
    fun signUp(
        @Header("Authorization") accessToken : String,
        @Body signUp: SignUpRequest
    ) : Call<EmptyResponse>

    @Headers("No-Authorization: true")
    @POST("members/refresh")
    fun authRecreate(
        @Body authRecreate : AuthRecreateRequest
    ) : Call<EmptyResponse>

    @GET("members/information")
    fun getInfo(): Call<GetInfoResponse>
}
