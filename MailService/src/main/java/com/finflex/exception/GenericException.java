package com.finflex.exception;

import lombok.Getter;

@Getter
public class GenericException extends RuntimeException {
    private final ErrorType errorType;

    public GenericException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
