package com.finflex.entitiy;

import com.finflex.entitiy.enums.CustomerType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "customers")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"accounts", "transactions"})
public class Customer extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false, unique = true)
    private Long customerNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType;

    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Embedded
    private Address address;

    @Column(unique = true)
    private String VKN;

    @Column(unique = true)
    private String TCKN;

    @Column(unique = true)
    private String YKN;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

}
