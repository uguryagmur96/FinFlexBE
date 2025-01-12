package com.finflex.dto.response;

import com.finflex.entitiy.Address;
import com.finflex.entitiy.enums.CustomerType;
import lombok.Data;

@Data
public class CustomerResponse {

    private CustomerType customerType;
    private Long customerNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String fullAddress;
    private String city;
    private String district;
    private String postalCode;
    private String TCKN;
    private String VKN;
    private String YKN;

}
