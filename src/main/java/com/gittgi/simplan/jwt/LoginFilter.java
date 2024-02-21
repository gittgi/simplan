package com.gittgi.simplan.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gittgi.simplan.dto.CustomUserDetails;
import com.gittgi.simplan.dto.TokenDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {



    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication start");
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("attempt Authentication, username : {}", username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();


        String accessToken = jwtUtil.createJwt(username, role, 10*60*60*1000L, "ATK");
        String refreshToken = jwtUtil.createJwt(username, role, 10*60*60*1000*24L, "RTK");
        TokenDTO tokenDto = new TokenDTO();
        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);
        String result = objectMapper.writeValueAsString(tokenDto);
        response.getWriter().write(result);

        redisTemplate.opsForValue().set(
                username,
                refreshToken,
                60*60*1000*24L,
                TimeUnit.MILLISECONDS
        );

        log.info("Normal login success");

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        //TODO: Login 실패시, 실패 원인을 request에 exception 헤더에 넣고, 그걸 꺼내서 적절한 response로 반환하기
        log.info("login fail");
        response.setStatus(401);
    }
}
