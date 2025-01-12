package com.finflex.controller;


import com.finflex.dto.AuthRequest;
import com.finflex.dto.AuthResponse;
import com.finflex.dto.ChangePasswordDto;
import com.finflex.dto.ForgetPasswordRequest;
import com.finflex.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.finflex.constants.RestApiList.*;

@RestController
@CrossOrigin("*")
@RequestMapping(AUTH)
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @Operation(
            summary = "Login to system"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error"
            )
    }
    )
    @PostMapping(LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping(CHANGE_PASSWORD)
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        String response = authService.changePassword(changePasswordDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordRequest request) {
        String response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }
}
