package com.nhk.fx.exception;

import com.nhk.fx.exception.model.BusinessException;
import com.nhk.fx.exception.model.ErrorResponse;
import com.nhk.fx.exception.model.UpstreamException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.of(ex.getStatus(),  ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest req) {

        var fieldError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        String msg = (fieldError != null) ? fieldError.getField() + ": " + fieldError.getDefaultMessage() : "Validation error";

        return ResponseEntity.badRequest().body(
                ErrorResponse.of(HttpStatus.BAD_REQUEST,  msg, req.getRequestURI())
        );
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            jakarta.validation.ConstraintViolationException ex, HttpServletRequest req) {

        var violation = ex.getConstraintViolations().stream().findFirst().orElse(null);
        String msg = (violation != null) ? violation.getPropertyPath() + ": " + violation.getMessage() : "Validation error";

        return ResponseEntity.badRequest().body(
                ErrorResponse.of(HttpStatus.BAD_REQUEST,  msg, req.getRequestURI())
        );
    }

    @ExceptionHandler(UpstreamException.class)
    public ResponseEntity<ErrorResponse> handleUpstream(UpstreamException e, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                ErrorResponse.of(HttpStatus.BAD_GATEWAY, "Upstream (OANDA) failure", req.getRequestURI())
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(HttpStatus.BAD_REQUEST,  ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception {} {}",
                req.getMethod(), req.getRequestURI(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI()));
    }
}
