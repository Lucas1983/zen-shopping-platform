package com.zen.ala.infrastructure.web.exception;

import com.zen.ala.domain.error.InvalidDiscountStrategyException;
import com.zen.ala.domain.error.ProductNotFoundException;
import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for product-related exceptions.
 *
 * <p>This class handles exceptions thrown by the product service and returns appropriate HTTP
 * responses.
 */
@RestControllerAdvice
public class ProductExceptionHandler {

  /**
   * Handles ProductNotFoundException and returns a 404 Not Found response.
   *
   * @param ex the exception thrown
   * @return a ResponseEntity with the error message and HTTP status
   */
  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleProductNotFound(ProductNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(errorResponse(ex.getMessage(), HttpStatus.NOT_FOUND));
  }

  /**
   * Handles InvalidDiscountStrategyException and returns a 400 Bad Request response.
   *
   * @param ex the exception thrown
   * @return a ResponseEntity with the error message and HTTP status
   */
  @ExceptionHandler(InvalidDiscountStrategyException.class)
  public ResponseEntity<Map<String, Object>> handleInvalidDiscountStrategy(
      InvalidDiscountStrategyException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST));
  }

  /**
   * Handles all other exceptions and returns a 500 Internal Server Error response.
   *
   * @param ex the exception thrown
   * @return a ResponseEntity with the error message and HTTP status
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse("Unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  private Map<String, Object> errorResponse(String message, HttpStatus status) {
    return Map.of(
        "timestamp", Instant.now().toString(),
        "status", status.value(),
        "error", status.getReasonPhrase(),
        "message", message);
  }
}
