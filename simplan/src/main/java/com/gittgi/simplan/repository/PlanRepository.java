package com.gittgi.simplan.repository;

import com.gittgi.simplan.entity.PlanEntity;
import com.gittgi.simplan.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PlanRepository extends JpaRepository<PlanEntity, Integer> {


    @Query(value = "select p from plan p where p.user =:user and p.planStartTime between :start and :end")
    List<PlanEntity> findDailyPlan(UserEntity user, LocalDateTime start, LocalDateTime end);

}
