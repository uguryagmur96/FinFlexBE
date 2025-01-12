package com.finflex.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(MailCouldNotSentException.class)
    public ResponseEntity<ExceptionResponse> handleMailCouldNotSentException(MailCouldNotSentException exception) {
        return createExceptionResponse(exception);
    }

    private ResponseEntity<ExceptionResponse> createExceptionResponse(GenericException exception) {
        ExceptionResponse response = ExceptionResponse.builder()
                .code(exception.getErrorType().getCode())
                .message(exception.getErrorType().getMessage())
                .status(exception.getErrorType().getHttpStatus())
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.status(exception.getErrorType().getHttpStatus()).body(response);
    }
}
