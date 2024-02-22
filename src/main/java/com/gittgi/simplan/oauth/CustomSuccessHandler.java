package com.gittgi.simplan.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gittgi.simplan.dto.CustomOAuth2User;
import com.gittgi.simplan.dto.TokenDTO;
import com.gittgi.simplan.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();


    // 로그인 성공시 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();;
        String role = auth.getAuthority();


        //TODO:토큰 발급 및 respponse 만드는 로직 모듈화 하기
        String accessToken = jwtUtil.createJwt(username, role, 10*60*60*1000L, "ATK");
        String refreshToken = jwtUtil.createJwt(username, role, 10*60*60*1000*24L, "RTK");
        TokenDTO tokenDto = new TokenDTO();
        tokenDto.setAccessToken(accessToken);

        String result = objectMapper.writeValueAsString(tokenDto);
        response.setHeader(HttpHeaders.SET_COOKIE, createCookie(refreshToken).toString());
        response.getWriter().write(result);

        //TODO:Redirect 어디로 설정해야 하는지 확인하기
        response.sendRedirect("https://simplan-next.vercel.app/Today");

        redisTemplate.opsForValue().set(
                username,
                refreshToken,
                60*60*1000*24L,
                TimeUnit.MILLISECONDS
        );

        log.info("OAuth login success");


    }


    private ResponseCookie createCookie(String value) {
        return ResponseCookie
                .from("RefreshToken", value)
                .path("/reissue")
                .httpOnly(true)
                .secure(true)
                .sameSite("none")
                .maxAge(Duration.ofDays(1))
                .build();
    }

}
