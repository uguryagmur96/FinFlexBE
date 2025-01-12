package com.finflex.dto;

import com.finflex.entity.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
public class UserResponseDto {

    private String userName;
    private String firstName;
    private String lastName;
    private String mailAddress;
    private String tckn;
    private String address;
    private String personelNumber;
    private UserStatus userStatus;

}
