package com.gittgi.simplan.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {
    private String message;
    private T data;
}
