package com.project.how.network.api_interface

import com.project.how.data_class.dto.EmptyResponse
import com.project.how.data_class.dto.LoginRequest
import com.project.how.data_class.dto.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface MemberService {
    @POST("members/login")
    fun login(
        @Body login: LoginRequest
    ) : Call<String>

    @PUT("members/signup")
    fun signUp(
        @Header("Bearer Token") accessToken : String,
        @Body signUp: SignUpRequest
    ) :Call<EmptyResponse>
}
