package com.gittgi.simplan.error.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PlanErrorCode implements ErrorCode{

    PLAN_NOT_FOUND("PL_001", "해당 일정을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    TITLE_REQUIRED("PL_002", "제목은 필수 입니다.",HttpStatus.BAD_REQUEST),
    CATEGORY_REQUIRED("PL_003", "카테고리는은 필수 입니다.",HttpStatus.BAD_REQUEST),
    PLANSTARTTIME_REQUIRED("PL_004", "시작시간은 필수 입니다.",HttpStatus.BAD_REQUEST),
    PLANENDTIMEREQUIRED("PL_005", "종료시간은 필수 입니다.",HttpStatus.BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
