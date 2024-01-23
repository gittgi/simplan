package com.gittgi.simplan.service;

import com.gittgi.simplan.dto.UserParameterDTO;
import com.gittgi.simplan.dto.request.PlanPostRequestDto;
import com.gittgi.simplan.dto.response.PlanResponseDto;
import com.gittgi.simplan.entity.PlanEntity;
import com.gittgi.simplan.entity.UserEntity;
import com.gittgi.simplan.repository.PlanRepository;
import com.gittgi.simplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    public Long savePlan(String username, PlanPostRequestDto planRequest) {
        UserEntity user = userRepository.findByUsername(username);
        PlanEntity plan = makePlan(planRequest, user);
        return planRepository.save(plan).getId();
    }




    public Map<Integer, List<PlanResponseDto>> getDailySchedule(UserParameterDTO userParameterDTO, LocalDate date) {
        UserEntity user = userRepository.findByUsername(userParameterDTO.getUsername());
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<PlanEntity> dailyPlan = planRepository.findDailyPlan(user, start, end);
        int day = date.getDayOfMonth();
        List<PlanResponseDto> planResponseDtoList = makePlanResponseDtoList(dailyPlan);
        Map<Integer, List<PlanResponseDto>> dailySchedule = new HashMap<>();
        dailySchedule.put(day, planResponseDtoList);
        return dailySchedule;


    }



    public Map<Integer, List<PlanResponseDto>> getWeeklySchedule(UserParameterDTO userParameterDTO, LocalDate date) {
        UserEntity user = userRepository.findByUsername(userParameterDTO.getUsername());
        int day = date.get(ChronoField.DAY_OF_WEEK);
        if (day == 7) {
            day = 0;
        }
        LocalDate start = date.minusDays(day);
        LocalDate end = start.plusDays(6);
        LocalDateTime weekStart = start.atStartOfDay();
        LocalDateTime weekEnd = end.atTime(LocalTime.MAX);

        List<PlanEntity> weeklyPlan = planRepository.findDailyPlan(user, weekStart, weekEnd);
        List<PlanResponseDto> tempWeeklyPlan = makePlanResponseDtoList(weeklyPlan);

        return tempWeeklyPlan.stream().collect(Collectors.groupingBy(p -> p.getPlanStartTime().getDayOfMonth()));

    }

    public Map<Integer, List<PlanResponseDto>> getMonthlySchedule(UserParameterDTO userParameterDTO, LocalDate date) {
        UserEntity user = userRepository.findByUsername(userParameterDTO.getUsername());

        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        LocalDateTime monthStart = start.atStartOfDay();
        LocalDateTime monthEnd = end.atTime(LocalTime.MAX);

        List<PlanEntity> monthlyPlan = planRepository.findDailyPlan(user, monthStart, monthEnd);
        List<PlanResponseDto> tempMonthlyPlan = makePlanResponseDtoList(monthlyPlan);

        return tempMonthlyPlan.stream().collect(Collectors.groupingBy(p -> p.getPlanStartTime().getDayOfMonth()));

    }

    private List<PlanResponseDto> makePlanResponseDtoList(List<PlanEntity> dailyPlan) {
        List<PlanResponseDto> planResponseDtoList = dailyPlan.stream().map(planEntity -> {
                    PlanResponseDto dto = new PlanResponseDto();
                    dto.setId(planEntity.getId());
                    dto.setTitle(planEntity.getTitle());
                    dto.setContent(planEntity.getContent());
                    dto.setIsImportant(planEntity.getIsImportant());
                    dto.setPlanStartTime(planEntity.getPlanStartTime());
                    dto.setPlanEndTime(planEntity.getPlanEndTime());
                    dto.setRealStartTime(planEntity.getRealStartTime());
                    dto.setRealEndTime(planEntity.getRealEndTime());
                    dto.setStatus(planEntity.getStatus());
                    dto.setCategory(planEntity.getCategory());
                    return dto;
                })
                .toList();
        return planResponseDtoList;
    }








    private PlanEntity makePlan(PlanPostRequestDto planRequest, UserEntity user) {
        PlanEntity plan = new PlanEntity();
        plan.setTitle(planRequest.getTitle());
        plan.setContent(planRequest.getContent());
        plan.setIsImportant(planRequest.getIsImportant());
        plan.setCategory(planRequest.getCategory());
        plan.setPlanStartTime(planRequest.getPlanStartTime());
        plan.setPlanEndTime(planRequest.getPlanEndTime());
        plan.setUser(user);
        return plan;
    }


}
