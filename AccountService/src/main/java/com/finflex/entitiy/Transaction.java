package com.finflex.entitiy;

import com.finflex.entitiy.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private BigDecimal transactionCurrRate;
    private BigDecimal transactionFee;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType sourceCurrType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType targetCurrType;
    @Column(nullable = false)
    private BigDecimal sourceAmount;
    @Column(nullable = false)
    private BigDecimal targetAmount;
    @Column(nullable = false)
    private String userTCKN;
    private Long userNo;
    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

}
