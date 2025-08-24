package com.nhk.fx.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now().toString(),
                ex.getStatus(),
                ex.getError(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        var fieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String msg = (fieldError != null) ? fieldError.getField() + ": " + fieldError.getDefaultMessage() : "Validation error";

        return ResponseEntity.badRequest().body(
                ErrorResponse.of(400, "Bad Request", msg, req.getRequestURI())
        );
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            jakarta.validation.ConstraintViolationException ex, HttpServletRequest req) {

        var violation = ex.getConstraintViolations().stream().findFirst().orElse(null);
        String msg = (violation != null) ? violation.getPropertyPath() + ": " + violation.getMessage() : "Validation error";

        return ResponseEntity.badRequest().body(
                ErrorResponse.of(400, "Bad Request", msg, req.getRequestURI())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                OffsetDateTime.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                ""
        );

        log.error("Unhandled exception {} {}",
                req.getMethod(), req.getRequestURI(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
