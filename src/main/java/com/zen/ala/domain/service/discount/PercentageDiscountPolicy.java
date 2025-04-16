package com.zen.ala.domain.service.discount;

import java.math.BigDecimal;

/**
 * A discount policy that applies a percentage discount to the base price.
 *
 * <p>This class implements the {@link DiscountPolicy} interface and provides a method to apply a
 * percentage discount to a given base price based on the specified discount rate.
 */
public class PercentageDiscountPolicy implements DiscountPolicy {

  private final BigDecimal discountRate;

  public PercentageDiscountPolicy(BigDecimal discountRate) {
    this.discountRate = discountRate;
  }

  @Override
  public BigDecimal apply(BigDecimal basePrice, int quantity) {
    return basePrice.subtract(basePrice.multiply(discountRate));
  }
}
