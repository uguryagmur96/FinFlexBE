package com.finflex.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {

    @Pattern(regexp = "(^$|[1-9]{11})", message="Invalid identity")
    private String customerTckn;
    @Pattern(regexp = "(^$|[1-9]{11})", message="Invalid identity")
    private String customerYkn;

    @Pattern(regexp = "(^$|[1-9]{10})", message="Invalid VKN")
    private String customerVkn;

    @NotNull(message = "Source amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Source amount must be greater than zero")
    private BigDecimal Amount;

    @Pattern(regexp = "(^$|[1-9]{11})", message="Invalid identity")
    private String userTCKN;

    @Pattern(regexp = "^\\d{6}$", message = "Invalid User Number")
    private Long userNo;

    private Long sourceAccountNo;

    private Long targetAccountNo;

}
