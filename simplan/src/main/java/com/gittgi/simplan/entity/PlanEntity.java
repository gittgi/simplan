package com.gittgi.simplan.entity;

import com.gittgi.simplan.domain.PlanCategory;
import com.gittgi.simplan.domain.PlanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity(name = "plan")
@Getter
@Setter
public class PlanEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanStatus status = PlanStatus.DEFAULT;


    private String content;


    private Boolean isImportant = false;

    @Column(nullable = false)
    private LocalDateTime planStartTime;

    @Column(nullable = false)
    private LocalDateTime planEndTime;


    private LocalDateTime realStartTime;

    private LocalDateTime realEndTime;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;



}
