package com.gittgi.simplan.dto.request;

import com.gittgi.simplan.domain.PlanCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlanPostRequestDto {
    private String title;
    private String content;
    private PlanCategory category;
    private Boolean isImportant;
    private LocalDateTime planStartTime;
    private LocalDateTime planEndTime;
}
