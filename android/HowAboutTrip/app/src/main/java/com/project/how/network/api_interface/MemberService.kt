package com.project.how.network.api_interface

import com.project.how.data_class.dto.LoginRequest
import com.project.how.data_class.dto.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MemberService {
    @POST("members/login")
    fun login(
        @Body login: LoginRequest
    ) : Call<String>

    @POST("members/signup")
    fun signUp(
        @Body signUp: SignUpRequest
    )
}
