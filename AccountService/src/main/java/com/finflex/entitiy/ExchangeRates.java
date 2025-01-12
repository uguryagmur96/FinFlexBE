package com.finflex.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "exchangerates")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExchangeRates  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currencyPair;
    private BigDecimal rate;

    public ExchangeRates(String pair, BigDecimal bigDecimal) {
        this.currencyPair=pair;
        this.rate=bigDecimal;
    }
}
