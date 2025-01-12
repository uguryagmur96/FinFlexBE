package com.finflex.entitiy;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String fullAddress;
    private String city;
    private String district;
    private String postalCode;

}
