package com.finflex.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotNull(message = "User name cannot be empty")
    @Schema(example = "John")
    private String firstName;

    @NotNull(message = "User last name cannot be empty")
    @Schema(example = "Doe")
    private String lastName;

    @NotNull(message = "email cannot be empty")
    @Pattern(regexp = "(^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$)",message = "invalid email address")
    @Schema(example = "johndoe@gmail.com")
    private String mailAddress;

    @NotNull(message = "identity cannot be empty")
    @Pattern(regexp="(^[1-9][0-9]{10}$)",message="invalid identity")
    private String tckn;
    @NotNull
    @Schema(example = "Kılıçali Paşa Mh. Beyoğlu/İstanbul PK:34433")
    private String address;

}
