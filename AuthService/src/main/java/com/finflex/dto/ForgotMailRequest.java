package com.finflex.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotMailRequest {
    private String token;
    private String sentToMailAddress;
}
