package com.gittgi.simplan.controller;

import com.gittgi.simplan.annotation.Login;
import com.gittgi.simplan.dto.JoinDTO;
import com.gittgi.simplan.dto.TokenDTO;
import com.gittgi.simplan.dto.UserParameterDTO;
import com.gittgi.simplan.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO) {

        authService.joinProcess(joinDTO);



        return "ok";
    }

    @PostMapping("/reissue")
    public TokenDTO reissueToken(HttpServletRequest request, HttpServletResponse response, @CookieValue("RefreshToken") String refreshToken) {

        //Authorization 헤더 검증
        if (refreshToken == null) {

            log.info("토큰 없음");

            //조건이 해당되면 메소드 종료 (필수)
            throw new RuntimeException("token null");
        }


        return authService.generateNewToken(refreshToken, response);

    }

    @GetMapping("/test")
    public String test(@Login UserParameterDTO userParameterDto) {
        log.info(userParameterDto.getUsername());
        return userParameterDto.getUsername();
    }

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "ok";
    }

}
