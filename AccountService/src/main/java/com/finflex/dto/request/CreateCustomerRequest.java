package com.finflex.dto.request;

import com.finflex.entitiy.enums.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class CreateCustomerRequest {

    private CustomerType customerType;
    private String firstName;
    private String lastName;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @Pattern(
            regexp = "^0?[1-9]\\d{9,14}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    private String fullAddress;
    private String city;
    private String district;

    @Pattern(
            regexp = "^[A-Za-z0-9\\s-]{3,10}$",
            message = "Invalid postal code"
    )
    private String postalCode;

    @Pattern(regexp = "(^$|[1-9]{10})", message="Invalid VKN")
    private String VKN;

    @Pattern(regexp = "(^$|[1-9]{11})", message="Invalid identity")
    private String TCKN;

    @Pattern(regexp = "(^$|[1-9]{11})", message="Invalid identity")
    private String YKN;

    private List<CreateAccountRequest> accounts;

}
