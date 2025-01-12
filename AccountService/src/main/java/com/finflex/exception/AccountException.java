package com.finflex.exception;

import lombok.Getter;

@Getter
public class AccountException extends RuntimeException {
    private final ErrorType errorType;

    public AccountException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public AccountException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }
}
