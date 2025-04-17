package com.zen.ala.domain.error;

import java.util.UUID;

/**
 * Exception thrown when a product with a given ID is not found.
 *
 * <p>This exception is used to indicate that a product with the specified ID does not exist in the
 * system.
 */
public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(UUID id) {
    super("Product with id " + id + " not found.");
  }
}
