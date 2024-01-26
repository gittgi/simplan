package com.gittgi.simplan.controller;

import com.gittgi.simplan.annotation.Login;
import com.gittgi.simplan.dto.JoinDTO;
import com.gittgi.simplan.dto.TokenDTO;
import com.gittgi.simplan.dto.UserParameterDTO;
import com.gittgi.simplan.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

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
    public TokenDTO reissueToken(HttpServletRequest request) {
        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("토큰 없음");

            //조건이 해당되면 메소드 종료 (필수)
            throw new RuntimeException("token null");
        }

        String refreshToken = authorization.split(" ")[1];

        return authService.generateNewToken(refreshToken);

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
