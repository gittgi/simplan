package com.gittgi.simplan.service;

import com.gittgi.simplan.dto.CustomOAuth2User;
import com.gittgi.simplan.dto.OAuth2Response;
import com.gittgi.simplan.dto.UserDto;
import com.gittgi.simplan.dto.response.NaverResponse;
import com.gittgi.simplan.entity.UserEntity;
import com.gittgi.simplan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User : {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("registrationId : {}", registrationId);
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        log.info("username : {}", username);
        UserEntity existData = userRepository.findByUsername(username);
        if (existData == null) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            //TODO: nickname의 경우, 네이버 계정이 따로 닉네임을 설정해두지 않으면 네이버 ID를 마스킹 해서 리턴해줌(gi**** 같이), 따라서 닉네임 변경 기능을 마련하는게 좋을 듯
            userEntity.setNickname(oAuth2Response.getNickname());
            userEntity.setPassword("");
            userEntity.setRole("ROLE_USER");
            userEntity.setSocial(oAuth2Response.getProvider());

            userRepository.save(userEntity);

            UserDto userDto = new UserDto();
            userDto.setUsername(username);
            userDto.setNickname(oAuth2Response.getNickname());
            userDto.setRole("ROLE_USER");
            log.info("if userDto : {}", userDto);
            return new CustomOAuth2User(userDto);

        } else {
            existData.setNickname(oAuth2Response.getNickname());
            userRepository.save(existData);

            UserDto userDto = new UserDto();
            userDto.setUsername(existData.getUsername());
            userDto.setNickname(oAuth2Response.getNickname());
            userDto.setRole(existData.getRole());
            log.info("else userDto : {}", userDto);
            return new CustomOAuth2User(userDto);


        }





    }
}
