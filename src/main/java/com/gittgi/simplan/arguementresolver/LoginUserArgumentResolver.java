package com.gittgi.simplan.arguementresolver;

import com.gittgi.simplan.annotation.Login;
import com.gittgi.simplan.dto.UserParameterDTO;
import com.gittgi.simplan.entity.UserEntity;
import com.gittgi.simplan.jwt.JWTUtil;
import com.gittgi.simplan.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JWTUtil jwtUtil;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasUserType = UserParameterDTO.class.isAssignableFrom(parameter.getParameterType());



        return hasLoginAnnotation && hasUserType;
    }

    @Override
    public UserParameterDTO resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("토큰 없음");

            //토큰이 없으면 null
            return null;
        }

        String token = authorization.split(" ")[1];
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        return new UserParameterDTO(username, role);


    }
}
