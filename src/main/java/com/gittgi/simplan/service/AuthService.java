package com.gittgi.simplan.service;


import com.gittgi.simplan.dto.JoinDTO;
import com.gittgi.simplan.dto.TokenDTO;
import com.gittgi.simplan.entity.UserEntity;
import com.gittgi.simplan.error.code.AccountErrorCode;
import com.gittgi.simplan.error.exception.ErrorException;
import com.gittgi.simplan.jwt.JWTUtil;
import com.gittgi.simplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public void joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            throw new ErrorException(AccountErrorCode.ID_DUPLICATION);
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setNickname(joinDTO.getNickname());
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }

    public TokenDTO generateNewToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        String reservedToken = redisTemplate.opsForValue().get(username);
        if (reservedToken == null || !refreshToken.equals(reservedToken)) {
            throw new RuntimeException("token wrong");
        } else if (jwtUtil.isExpired(refreshToken)) {
            throw new RuntimeException("token expired");
        }

        // 새로운 accessToken과 refreshToken 생성
        String newAccessToken = jwtUtil.createJwt(username, role, 10 * 60 * 60 * 1000L, "ATK");
        String newRefreshToken = jwtUtil.createJwt(username, role, 10 * 60 * 60 * 1000 * 24L, "RTK");

        TokenDTO tokenDto = new TokenDTO();
        tokenDto.setAccessToken(newAccessToken);
        tokenDto.setRefreshToken(newRefreshToken);

        redisTemplate.opsForValue().set(
                username,
                newRefreshToken,
                10*60*60*1000*24L,
                TimeUnit.MILLISECONDS
        );



        return tokenDto;
    }


}
