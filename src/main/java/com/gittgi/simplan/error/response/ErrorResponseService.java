package com.gittgi.simplan.error.response;

import com.gittgi.simplan.dto.response.BaseResponse;
import com.gittgi.simplan.error.code.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ErrorResponseService {

    private BaseResponse<ErrorResponse> makeErrorResponse(ErrorCode errorCode) {


        BaseResponse<ErrorResponse> res = new BaseResponse<>();
        res.setMessage("실패");
        res.setData(
                ErrorResponse.builder()
                        .code(errorCode.getCode())
                        .error(errorCode.name())
                        .message(errorCode.getMessage())
                        .build()
        );
        return res;
    }

    public ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        log.error("[ErrorResponse] {}: {} {} {}", errorCode.getHttpStatus(), errorCode.getCode(), errorCode.name(), errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }



}
