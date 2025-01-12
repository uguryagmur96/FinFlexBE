package com.finflex.exception;

import lombok.Getter;

@Getter
public class MailCouldNotSentException extends GenericException {
    public MailCouldNotSentException(ErrorType errorType) {
        super(errorType);
    }
}
