package com.finflex.entitiy;

import com.finflex.entitiy.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"customer", "transactions"})
public class Account extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    @Column(nullable = false, unique = true)
    private Long accountNo;

    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currencyType;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
}
