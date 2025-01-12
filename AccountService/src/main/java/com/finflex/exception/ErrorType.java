package com.finflex.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_SERVER_ERROR(1000,"Sunucuda Bilinmeyen bir hata oluştu", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_TOKEN(1001,"Geçersiz token",HttpStatus.BAD_REQUEST),
    INVALID_USER(1002, "Geçersiz kullanıcı", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(1003,"İstek formatı hatalı",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_FOUND(1004, "Account Not Found", HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND(1005, "Customer Not Found", HttpStatus.NOT_FOUND),
    INSUFFICIENT_BALANCE(1006, "Balance is not enough for this transaction", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOT_FOUND(1007, "Transaction Not Found", HttpStatus.NOT_FOUND),
    CUSTOMER_ALREADY_EXISTS(1008, "Customer Already Exists", HttpStatus.CONFLICT),
    INDIVIDUAL_CUSTOMER_TYPE_MISMATCH(1009, "Individual customer must have either TCKN or YKN", HttpStatus.BAD_REQUEST),
    CORPORATE_CUSTOMER_TYPE_MISMATCH(1010, "Corporate customer must have a VKN", HttpStatus.BAD_REQUEST),
    CUSTOMER_ALREADY_HAS_THIS_ACCOUNT(1011, "Customer already has this account", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1012, "Email already exists", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXISTS(1013, "Phone number already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1014, "Personnel Not Found", HttpStatus.NOT_FOUND),
    INVALID_NUMBER_OF_IDENTITY(1015, "Exactly one of VKN, TCKN, or YKN must be provided.", HttpStatus.BAD_REQUEST),
    ACCOUNT_CANNOT_BE_DELETED(1016, "Since account balance is not zero, it cannot be deleted.", HttpStatus.BAD_REQUEST),
    TRY_ACCOUNT_CANNOT_BE_DELETED(1017, "TRY Account Cannot Be Deleted", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus httpStatus;

}
