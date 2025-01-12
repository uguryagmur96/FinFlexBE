package com.finflex.dto;


import com.finflex.entity.EUserType;
import com.finflex.entity.UserStatus;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private EUserType userType;
    private UserStatus status;
}
