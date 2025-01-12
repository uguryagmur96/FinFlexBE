package com.finflex.dto.request;

import com.finflex.entitiy.enums.CurrencyType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    @NotNull
    private CurrencyType currencyType;
    private Long customerNumber;
    private BigDecimal balance = BigDecimal.ZERO;

}
