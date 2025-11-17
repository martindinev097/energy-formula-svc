package com.buildingenergy.energy_formula_svc.web;

import com.buildingenergy.energy_formula_svc.exception.ApiKeyMissingException;
import com.buildingenergy.energy_formula_svc.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(ApiKeyMissingException.class)
    public ResponseEntity<ErrorResponse> handleApiKeyMissingException(ApiKeyMissingException ex) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

}
