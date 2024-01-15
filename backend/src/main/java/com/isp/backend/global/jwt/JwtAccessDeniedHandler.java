package com.isp.backend.global.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 인가되지 않은(권한이 없는) 요청이 들어왔을 때의 처리를 담당 - `handle` 메서드는 예외 처리 및 로깅을 수행한다.
        log.error("Forbidden Request : {}", request.getRequestURI());
        response.sendError(HttpServletResponse.SC_FORBIDDEN);

    }

}
