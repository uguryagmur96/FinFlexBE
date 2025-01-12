package com.finflex.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetExchangeRatesRequest {
    private String result;
    private String base_code;
    private String target_code;
    private BigDecimal conversion_rate;
}
