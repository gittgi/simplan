package com.gittgi.simplan.error.handler;

import com.gittgi.simplan.error.code.ErrorCode;
import com.gittgi.simplan.error.exception.ErrorException;
import com.gittgi.simplan.error.response.ErrorResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorExceptionHandler {

    private final ErrorResponseService responseService;

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Object> handleCustomException(final ErrorException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return responseService.handleExceptionInternal(errorCode);
    }

}
