package com.finflex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotNull(message = "User last name cannot be empty")
    @Schema(example = "jdoe1 (first letter of firstname+last name+sufflix)")
    private String username;
    @NotNull
    private String oldPassword;
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
    private String newPassword;
    @NotNull
    private String token;
}
