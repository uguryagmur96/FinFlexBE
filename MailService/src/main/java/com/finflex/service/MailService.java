package com.finflex.service;

import com.finflex.dto.ForgotMailRequest;
import com.finflex.dto.RegisterMailRequest;

public interface MailService {
    void sendRegisterMail(RegisterMailRequest mailRequest);
    void sendForgotMail(ForgotMailRequest mailRequest);
}
