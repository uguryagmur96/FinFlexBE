package com.finflex.dto.request;

import com.finflex.entitiy.enums.CurrencyType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountRequest {
    private Long accountNo;
    private CurrencyType currencyType;
    private BigDecimal balance;
}
