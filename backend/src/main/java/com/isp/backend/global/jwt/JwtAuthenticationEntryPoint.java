package com.isp.backend.global.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 인증되지 않은 요청이 들어왔을 때의 처리를 담당 - `commence` 메서드는 예외 처리 및 로깅을 수행한다.
        log.error("인증되지 않은 요청입니다. : {}", request.getRequestURI());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
