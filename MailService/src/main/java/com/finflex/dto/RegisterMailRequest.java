package com.finflex.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterMailRequest extends MailRequest{
    private String username;
    private String password;
}
