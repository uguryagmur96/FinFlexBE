package com.finflex.controller;

import com.finflex.dto.ForgotPasswordChangeRequest;
import com.finflex.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.finflex.constants.RestApiList.*;

@RestController
@RequestMapping(TOKENS)
@RequiredArgsConstructor
@CrossOrigin("*")
public class TokenController {
    private final TokenService tokenService;

    @PutMapping(VERIFY_USER_REGISTER)
    private ResponseEntity<String> verifyCreatedUser(@RequestParam String token) {
        String tokenVerifyResponse = tokenService.findTokenAndVerifyCreatedUser(token);
        return new ResponseEntity<>(tokenVerifyResponse, HttpStatus.OK);
    }

    @PutMapping(CHANGE_USER_PASSWORD)
    private ResponseEntity<String> forgotPasswordChange(@RequestBody ForgotPasswordChangeRequest passwordDto, @RequestParam String token) {
        String tokenForgotPasswordResponse = tokenService.findTokenAndChangePassword(passwordDto, token);
        return new ResponseEntity<>(tokenForgotPasswordResponse, HttpStatus.OK);
    }
}
