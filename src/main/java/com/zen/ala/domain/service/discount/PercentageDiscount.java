package com.zen.ala.domain.service.discount;

import java.math.BigDecimal;

/**
 * A discount that applies a percentage discount to the base price.
 *
 * <p>This class implements the {@link Discount} interface and provides a method to apply a
 * percentage discount to a given base price based on the specified discount rate.
 */
public class PercentageDiscount implements Discount {

  private final BigDecimal discountRate;

  public PercentageDiscount(BigDecimal discountRate) {
    this.discountRate = discountRate;
  }

  @Override
  public BigDecimal apply(BigDecimal basePrice, int quantity) {
    return basePrice.subtract(basePrice.multiply(discountRate));
  }
}
