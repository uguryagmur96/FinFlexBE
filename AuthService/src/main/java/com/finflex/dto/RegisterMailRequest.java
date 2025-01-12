package com.finflex.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterMailRequest {
    private String token;
    private String sentToMailAddress;
    private String username;
    private String password;
}
