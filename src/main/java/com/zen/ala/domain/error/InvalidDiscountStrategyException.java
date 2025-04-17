package com.zen.ala.domain.error;

/** Exception thrown when an invalid discount strategy is encountered. */
public class InvalidDiscountStrategyException extends RuntimeException {

  public InvalidDiscountStrategyException(String message) {
    super(message);
  }
}
