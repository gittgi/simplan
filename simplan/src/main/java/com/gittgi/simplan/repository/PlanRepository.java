package com.gittgi.simplan.repository;

import com.gittgi.simplan.entity.PlanEntity;
import com.gittgi.simplan.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {


    @Query(value = "select p from plan p where p.user =:user and p.planStartTime between :start and :end and p.deleted = false")
    List<PlanEntity> findDailyPlan(UserEntity user, LocalDateTime start, LocalDateTime end);


    @Override
    @Query(value = "select p from plan p where p.id =:planId and p.deleted = false")
    Optional<PlanEntity> findById(Long planId);
}
