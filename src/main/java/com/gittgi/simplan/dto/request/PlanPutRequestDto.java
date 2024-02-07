package com.gittgi.simplan.dto.request;

import com.gittgi.simplan.domain.PlanCategory;
import com.gittgi.simplan.domain.PlanStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlanPutRequestDto {
    private Long id;
    private String title;
    private String content;
    private PlanStatus status;
    private PlanCategory category;
    private Boolean isImportant;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
    private LocalDateTime realStartTime;
    private LocalDateTime realEndTime;
}
