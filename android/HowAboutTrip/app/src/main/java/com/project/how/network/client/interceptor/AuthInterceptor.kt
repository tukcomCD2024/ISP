package com.project.how.network.client.interceptor

import android.content.Context
import com.project.how.data_class.dto.member.AuthRecreateRequest
import com.project.how.model.MemberRepository
import com.project.how.view_model.MemberViewModel.authRecreate
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

        // "No-Authorization" 헤더가 있는지 확인
        val noAuth = originalRequest.header("No-Authorization") != null

        if (noAuth) {
            // "No-Authorization" 헤더 제거
            val requestBuilder = originalRequest.newBuilder()
                .removeHeader("No-Authorization")
            return chain.proceed(requestBuilder.build())
        } else {
            // 토큰 추가
            var requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "Bearer $accessToken")
            var request = requestBuilder.build()

            var response: Response = chain.proceed(request)
            if (response.code == 400) {
                response.close() // 기존 응답 닫기

                synchronized(this) {
                    val currentAccessToken = MemberRepository.tokensLiveData.value?.accessToken
                    if (accessToken == currentAccessToken) {
                        val refreshToken = MemberRepository.tokensLiveData.value?.refreshToken ?: ""
                        // 토큰 재생성 호출
                        val newToken = runBlocking {
                            try {
                                authRecreate(context, AuthRecreateRequest(refreshToken))
                            } catch (e: Exception) {
                                null
                            }
                        }

                        // 새로운 토큰으로 요청 다시 시도
                        if (newToken != null) {
                            requestBuilder = originalRequest.newBuilder()
                                .header("Authorization", "Bearer $newToken")
                            request = requestBuilder.build()
                            response = chain.proceed(request)
                        } else {
                            // 토큰 재발급 실패 시 실패 메시지 표시
                            throw IOException("Token recreate failed")
                        }
                    } else {
                        // 이미 다른 스레드에서 토큰이 갱신된 경우, 새로운 토큰으로 다시 시도
                        val newAccessToken = MemberRepository.tokensLiveData.value?.accessToken
                        if (newAccessToken != null) {
                            requestBuilder = originalRequest.newBuilder()
                                .header("Authorization", "Bearer $newAccessToken")
                            request = requestBuilder.build()
                            response = chain.proceed(request)
                        }
                    }
                }
            }
            return response
        }
    }
}
