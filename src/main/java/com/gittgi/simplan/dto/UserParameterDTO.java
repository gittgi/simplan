package com.gittgi.simplan.dto;

import lombok.Getter;

@Getter
public class UserParameterDTO {

    private String username;
    private String role;

    public UserParameterDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
