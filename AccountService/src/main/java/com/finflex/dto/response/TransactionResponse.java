package com.finflex.dto.response;

import com.finflex.entitiy.enums.CurrencyType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private CurrencyType sourceCurrType;
    private CurrencyType targetCurrType;
    private BigDecimal sourceAmount;
    private BigDecimal targetAmount;
    private String userTCKN;
    private Long userNo;
    private String customerTckn;
    private String customerFirstName;
    private String customerLastName;
    private BigDecimal transactionFee;
    private String customerNumber;
    private String accountNumber;
    private LocalDateTime transactionDate;

}
