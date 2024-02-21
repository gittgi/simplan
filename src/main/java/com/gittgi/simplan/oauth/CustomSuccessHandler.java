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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;


    // 로그인 성공시 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetail.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();;
        String role = auth.getAuthority();

        String accessToken = jwtUtil.createJwt(username, role, 10*60*60*1000L, "ATK");
        String refreshToken = jwtUtil.createJwt(username, role, 10*60*60*1000*24L, "RTK");

        response.addCookie(createCookie("AccessToken", accessToken));
        response.addCookie(createCookie("RefreshToken", refreshToken));

        response.sendRedirect("http://localhost:3000/");

        redisTemplate.opsForValue().set(
                username,
                refreshToken,
                60*60*1000*24L,
                TimeUnit.MILLISECONDS
        );

        log.info("OAuth login success");


    }


    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //https
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

}
