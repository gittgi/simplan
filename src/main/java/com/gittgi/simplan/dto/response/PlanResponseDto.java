package com.gittgi.simplan.dto.response;

import com.gittgi.simplan.domain.PlanCategory;
import com.gittgi.simplan.domain.PlanStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlanResponseDto {

    private Long id;

    private String title;
    private String content;
    private PlanCategory category;
    private PlanStatus status;
    private Boolean isImportant;


    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private LocalDateTime realStartTime;
    private LocalDateTime realEndTime;

}
