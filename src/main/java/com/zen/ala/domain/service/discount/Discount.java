package com.zen.ala.domain.service.discount;

import java.math.BigDecimal;

/**
 * Interface for discounts.
 *
 * <p>This interface defines a method for applying a discount to a base price based on the quantity
 * of items purchased.
 *
 * <p>Implementations of this interface can provide different discount strategies, such as
 * percentage-based discounts or quantity-based discounts.
 */
public interface Discount {

  /**
   * Applies the discount to the base price based on the specified quantity.
   *
   * @param basePrice the original price before discount
   * @param quantity the quantity of items purchased
   * @return the price after applying the discount
   */
  BigDecimal apply(BigDecimal basePrice, int quantity);
}
