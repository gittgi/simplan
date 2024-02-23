package com.gittgi.simplan.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gittgi.simplan.error.code.ErrorCode;
import com.gittgi.simplan.error.code.TokenErrorCode;
import com.gittgi.simplan.error.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증 실패시 결과를 처리해주는 로직을 가지고 있는 클래스
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("[commence] 인증 실패로 response.sendError 발생");

        String exception = String.valueOf(request.getAttribute("exception"));
        // 에러 상황을 try catch로 잡고 에러 내용을 request에 담을 것
        if (exception.equals(TokenErrorCode.EXPIRED_TOKEN.name())) {
            setResponse(response, TokenErrorCode.EXPIRED_TOKEN);
        } else if (exception.equals(TokenErrorCode.WRONG_TYPE_TOKEN.name())) {
            setResponse(response, TokenErrorCode.WRONG_TYPE_TOKEN);
        } else {
            setResponse(response, TokenErrorCode.UNKNOWN_ERROR);
        }
    }


    private void setResponse(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        log.error("[ErrorResponse] {}: {} {} {}", errorCode.getHttpStatus(), errorCode.getCode(), errorCode.name(), errorCode.getMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());

        response.getWriter().write(objectMapper.writeValueAsString(
                ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .error(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
        ));
    }
}