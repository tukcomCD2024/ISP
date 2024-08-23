package com.project.how.network.client.interceptor

import android.content.Context
import com.project.how.data_class.dto.member.AuthRecreateRequest
import com.project.how.model.MemberRepository
import com.project.how.view_model.MemberViewModel.authRecreate
import com.project.how.view_model.MemberViewModel.authRecreate2
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AuthInterceptor(@ApplicationContext private val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val accessToken = MemberRepository.tokensLiveData.value?.accessToken
        val noAuth = originalRequest.header("No-Authorization") != null

        // "No-Authorization" 헤더가 있을 경우 제거
        if (noAuth) {
            val requestBuilder = originalRequest.newBuilder()
                .removeHeader("No-Authorization")
            return chain.proceed(requestBuilder.build())
        }

        // 토큰 추가
        var requestBuilder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
        var response: Response = chain.proceed(requestBuilder.build())

        // 400 또는 401 응답 처리
        if (response.code == 400 || response.code == 401) {
            response.close() // 기존 응답 닫기

            synchronized(this) {
                val currentAccessToken = MemberRepository.tokensLiveData.value?.accessToken
                if (accessToken == currentAccessToken) {
                    val refreshToken = MemberRepository.tokensLiveData.value?.refreshToken ?: ""
                    val newToken = runBlocking {
                        try {
                            authRecreate2(context, AuthRecreateRequest(refreshToken))
                        } catch (e: Exception) {
                            null
                        }
                    }

                    // 새로운 토큰으로 요청 다시 시도
                    if (newToken != null) {
                        requestBuilder = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newToken")
                        response = chain.proceed(requestBuilder.build())
                    } else {
                        throw IOException("Token recreate failed")
                    }
                } else {
                    // 이미 다른 스레드에서 토큰이 갱신된 경우
                    val newAccessToken = MemberRepository.tokensLiveData.value?.accessToken
                    if (newAccessToken != null) {
                        requestBuilder = originalRequest.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                        response = chain.proceed(requestBuilder.build())
                    }
                }
            }
        }
        return response
    }
}
