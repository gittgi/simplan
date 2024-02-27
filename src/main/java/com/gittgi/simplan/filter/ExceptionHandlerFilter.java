package com.gittgi.simplan.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gittgi.simplan.error.code.ErrorCode;
import com.gittgi.simplan.error.exception.ErrorException;
import com.gittgi.simplan.error.response.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ErrorException e) {
            log.error("filter error : {}", e.getErrorCode().name());
            setResponse(response, e.getErrorCode());
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
