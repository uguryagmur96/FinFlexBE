package com.finflex.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String tckn;
    @Column
    private EUserType userType;
    @Column
    private String mailAddress;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column
    private String personelNumber;
    @Column
    @Lob
    private String address;

}
