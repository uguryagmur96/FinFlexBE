package com.finflex.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailRequest {
    private String token;
    private String sentToMailAddress;
}
