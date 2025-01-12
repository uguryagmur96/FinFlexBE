package com.finflex.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    MAIL_COULD_NOT_SENT(1100, "Mail gönderilemedi.", HttpStatus.BAD_REQUEST);

    final int code;
    final String message;
    final HttpStatus httpStatus;
}
