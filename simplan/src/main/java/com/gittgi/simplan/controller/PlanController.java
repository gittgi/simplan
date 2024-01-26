package com.gittgi.simplan.controller;

import com.gittgi.simplan.annotation.Login;
import com.gittgi.simplan.domain.PlanStatus;
import com.gittgi.simplan.dto.UserParameterDTO;
import com.gittgi.simplan.dto.request.PlanPostRequestDto;
import com.gittgi.simplan.dto.request.PlanPutRequestDto;
import com.gittgi.simplan.dto.response.BaseResponse;
import com.gittgi.simplan.dto.response.PlanResponseDto;
import com.gittgi.simplan.service.PlanService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private BaseResponse<Map<LocalDate, List<PlanResponseDto>>> getPlan(@Login UserParameterDTO userParameterDTO, @RequestParam String query, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {


        switch (query) {
            case "day":
                log.info("day");
                Map<LocalDate, List<PlanResponseDto>> dailySchedule = planService.getDailySchedule(userParameterDTO, date);
                BaseResponse<Map<LocalDate, List<PlanResponseDto>>> dayResponse = new BaseResponse<>();
                dayResponse.setData(dailySchedule);
                dayResponse.setMessage("ok");

                return dayResponse;
            case "week":
                log.info("week");
                Map<LocalDate, List<PlanResponseDto>> weeklySchedule = planService.getWeeklySchedule(userParameterDTO, date);
                BaseResponse<Map<LocalDate, List<PlanResponseDto>>> weekResponse = new BaseResponse<>();
                weekResponse.setData(weeklySchedule);
                weekResponse.setMessage("ok");

                return weekResponse;

            case "month":
                log.info("month");
                Map<LocalDate, List<PlanResponseDto>> monthlySchedule = planService.getMonthlySchedule(userParameterDTO, date);
                BaseResponse<Map<LocalDate, List<PlanResponseDto>>> monthResponse = new BaseResponse<>();
                monthResponse.setData(monthlySchedule);
                monthResponse.setMessage("ok");

                return monthResponse;
            default:
                log.info("not allowed");

                BaseResponse<Map<LocalDate, List<PlanResponseDto>>> baseResponse = new BaseResponse<>();
                baseResponse.setMessage("day, week, month 중에 하나를 선택하세요");
                return baseResponse;


        }

    }

    @PutMapping("/status/{planId}")
    private BaseResponse<Long> changeStatus(@Login UserParameterDTO userParameterdto, @PathVariable Long planId, @RequestBody Map<String, PlanStatus> map) {
        PlanStatus newStatus = map.get("newStatus");
        Long resultId = planService.changeStatus(userParameterdto, planId, newStatus);
        BaseResponse<Long> response = new BaseResponse<>();
        response.setMessage("상태가 " + newStatus + " 로 변경됨");
        response.setData(resultId);
        return response;


    }

    @PutMapping("/{planId}")
    private BaseResponse<Long> modifyPlan(@Login UserParameterDTO userParameterDto, @PathVariable Long planId, @RequestBody PlanPutRequestDto planPutRequestDto) {
        Long modifiedId = planService.modifyPlan(userParameterDto, planId, planPutRequestDto);
        BaseResponse<Long> baseResponse = new BaseResponse<>();
        baseResponse.setMessage("수정 성공");
        baseResponse.setData(modifiedId);
        return baseResponse;
    }

    @DeleteMapping("/{planId}")
    private BaseResponse<Long> deletePlan(@Login UserParameterDTO userParameterdto, @PathVariable Long planId) {
        Long deletedId = planService.deletePlan(userParameterdto, planId);
        BaseResponse<Long> baseResponse = new BaseResponse<>();
        baseResponse.setMessage("삭제 성공");
        baseResponse.setData(deletedId);
        return baseResponse;
    }



}
