package com.finflex.controller;

import com.finflex.dto.ForgotMailRequest;
import com.finflex.dto.MailRequest;
import com.finflex.dto.RegisterMailRequest;
import com.finflex.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.finflex.constants.RestApiList.*;

@RestController
@RequestMapping(MAIL)
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping(TEST_REGISTER_MAIL)
    public void testRegisterMail(@RequestBody RegisterMailRequest request){
        mailService.sendRegisterMail(request);
    }

    @PostMapping(TEST_FORGOT_MAIL)
    public void testForgotMail(@RequestBody ForgotMailRequest request) {
        mailService.sendForgotMail(request);
    }
}
