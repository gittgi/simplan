package com.gittgi.simplan.error.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
