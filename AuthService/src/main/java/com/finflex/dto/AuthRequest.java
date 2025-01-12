package com.finflex.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {

    @NotNull(message = "User last name cannot be empty")
    @Schema(example = "jdoe1 (first letter of firstname+last name+sufflix)")
    private String username;
    @NotNull(message = "password cannot be empty")
    private String password;
}