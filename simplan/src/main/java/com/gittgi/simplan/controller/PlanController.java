package com.gittgi.simplan.controller;

import com.gittgi.simplan.annotation.Login;
import com.gittgi.simplan.dto.UserParameterDTO;
import com.gittgi.simplan.dto.request.PlanPostRequestDto;
import com.gittgi.simplan.dto.response.BaseResponse;
import com.gittgi.simplan.dto.response.PlanResponseDto;
import com.gittgi.simplan.service.PlanService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.time.temporal.WeekFields.ISO;

@Slf4j
@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping
    private BaseResponse<Long> savePlan(@Login UserParameterDTO userParameterDTO, @RequestBody PlanPostRequestDto planPostRequestDto, HttpServletResponse response) {

        Long planId = planService.savePlan(userParameterDTO.getUsername(), planPostRequestDto);

        BaseResponse<Long> baseResponse = new BaseResponse<>();
        baseResponse.setMessage("ok");
        baseResponse.setData(planId);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return baseResponse;
    }



    @GetMapping
    private BaseResponse<Map<Integer, List<PlanResponseDto>>> getPlan(@Login UserParameterDTO userParameterDTO, @RequestParam String query, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {


        switch (query) {
            case "day":
                log.info("day");
                Map<Integer, List<PlanResponseDto>> dailySchedule = planService.getDailySchedule(userParameterDTO, date);
                BaseResponse<Map<Integer, List<PlanResponseDto>>> dayResponse = new BaseResponse<>();
                dayResponse.setData(dailySchedule);
                dayResponse.setMessage("ok");

                return dayResponse;
            case "week":
                log.info("week");
                Map<Integer, List<PlanResponseDto>> weeklySchedule = planService.getWeeklySchedule(userParameterDTO, date);
                BaseResponse<Map<Integer, List<PlanResponseDto>>> weekResponse = new BaseResponse<>();
                weekResponse.setData(weeklySchedule);
                weekResponse.setMessage("ok");

                return weekResponse;

            case "month":
                log.info("month");
                Map<Integer, List<PlanResponseDto>> monthlySchedule = planService.getMonthlySchedule(userParameterDTO, date);
                BaseResponse<Map<Integer, List<PlanResponseDto>>> monthResponse = new BaseResponse<>();
                monthResponse.setData(monthlySchedule);
                monthResponse.setMessage("ok");

                return monthResponse;
            default:
                log.info("not allowed");

                BaseResponse<Map<Integer, List<PlanResponseDto>>> baseResponse = new BaseResponse<>();
                baseResponse.setMessage("day, week, month 중에 하나를 선택하세요");
                return baseResponse;


        }

    }
}
