package com.finflex.dto.response;

import com.finflex.entitiy.enums.CurrencyType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {

    private BigDecimal balance;
    private CurrencyType currencyType;
    private Long accountNo;
    private String customerTckn;
    private String customerYkn;
    private String customerVkn;

}
