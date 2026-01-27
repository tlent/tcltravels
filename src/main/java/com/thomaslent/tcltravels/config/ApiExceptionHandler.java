package com.thomaslent.tcltravels.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thomaslent.tcltravels.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
    logger.warn("Resource not found: {}", ex.getMessage());
    Map<String, String> error = new HashMap<>();
    error.put("error", "Not Found");
    error.put("message", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    Map<String, Object> response = new HashMap<>();
    response.put("error", "Validation failed");
    response.put("fields", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Access Denied");
    error.put("message", "You don't have permission to access this resource");
    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
    logger.warn("Authentication failed: {}", ex.getMessage());
    Map<String, String> error = new HashMap<>();
    error.put("error", "Unauthorized");
    error.put("message", "Authentication failed. Please check your credentials.");
    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
    // Log full exception details server-side for debugging
    logger.error("Unexpected error occurred", ex);

    // Return generic message to client to avoid information disclosure
    Map<String, String> error = new HashMap<>();
    error.put("error", "Internal Server Error");
    error.put("message", "An unexpected error occurred. Please try again later.");
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
