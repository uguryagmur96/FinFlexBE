package com.finflex.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private Integer code;
    private String message;
    private HttpStatus status;
    private LocalDateTime time;
}
