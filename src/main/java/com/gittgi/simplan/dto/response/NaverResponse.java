package com.gittgi.simplan.dto.response;

import com.gittgi.simplan.dto.OAuth2Response;

import java.util.Map;


public class NaverResponse implements OAuth2Response {

    private final Map<String, Object> attiribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attiribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attiribute.get("id").toString();
    }

    @Override
    public String getNickname() {
        return attiribute.get("nickname").toString();
    }
}
