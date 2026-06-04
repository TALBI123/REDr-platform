package com.example.demo.common.exception;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.example.demo.common.dto.ApiError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handler for type mismatch in path variables or request parameters
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String message;
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            Object[] enumConstants = ex.getRequiredType().getEnumConstants();

            if (enumConstants != null && enumConstants.length > 0) {
                // Garder les valeurs en majuscules comme définies dans l'enum
                String allowedValues = String.join(", ",
                        Arrays.stream(enumConstants)
                                .map(Object::toString)
                                .toArray(String[]::new));

                message = String.format("Parameter '%s' has invalid value '%s'. Allowed values: %s",
                        ex.getName(),
                        ex.getValue(),
                        allowedValues);
            } else {
                message = String.format("Parameter '%s' has invalid value '%s'. Expected an enum type.",
                        ex.getName(),
                        ex.getValue());
            }
        } else {
            message = String.format("Parameter '%s' has invalid value: %s. Expected type: %s",
                    ex.getName(),
                    ex.getValue(),
                    ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        }

        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Handler for file upload size exceeded
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleMaxUploadSize(
            MaxUploadSizeExceededException ex, HttpServletRequest request) {

        long maxSize = ex.getMaxUploadSize();
        String maxSizeStr = maxSize / (1024 * 1024) + " MB";
        String message = String.format("File size exceeds maximum allowed size of %s", maxSizeStr);

        log.warn("Upload size exceeded: {}", ex.getMessage());
        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Handler for ConstraintViolationException (validation on path variables and
     * request params)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        log.warn("Constraint violation: {}", message);
        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiError> handleLocked(
            LockedException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiError> handleDisabled(
            DisabledException ex,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "Account is not active. Please check your email or contact support.",
                request.getRequestURI());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Invalid email or password", request.getRequestURI());
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiError> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException ex,
            HttpServletRequest request) {
        Throwable cause = ex.getCause();
        if (cause instanceof DisabledException) {
            return buildError(
                    HttpStatus.UNAUTHORIZED,
                    "Account is not active. Please check your email or contact support.",
                    request.getRequestURI());
        }
        if (cause instanceof LockedException) {
            return buildError(HttpStatus.UNAUTHORIZED, cause.getMessage(), request.getRequestURI());
        }

        log.error("Authentication service error", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error. Contact support.",
                request.getRequestURI());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuth(
            AuthenticationException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Invalid email or password", request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {
        return buildError(HttpStatus.FORBIDDEN, "Access denied", request.getRequestURI());
    }

    @ExceptionHandler(LazyInitializationException.class)
    public ResponseEntity<ApiError> handleLazyInitialization(
            LazyInitializationException ex,
            HttpServletRequest request) {
        log.error("Lazy initialization error - possible missing EAGER fetch", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error. Contact support.",
                request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(
            Exception ex,
            HttpServletRequest request) {
        log.error("Error on {} {}", request.getMethod(), request.getRequestURI(), ex);

        // Retournez l'exception réelle pour le debug
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", ex.getClass().getSimpleName(),
                        "message", ex.getMessage(),
                        "path", request.getRequestURI()));
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status, String message, String path) {
        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                Instant.now());

        return ResponseEntity.status(status).body(error);
    }
}