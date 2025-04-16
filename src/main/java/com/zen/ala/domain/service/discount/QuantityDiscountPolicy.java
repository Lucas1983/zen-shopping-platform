package com.zen.ala.domain.service.discount;

import java.math.BigDecimal;
import java.util.NavigableMap;

/**
 * A discount policy that applies a quantity-based discount to the base price.
 *
 * <p>This class implements the {@link DiscountPolicy} interface and provides a method to apply a
 * quantity-based discount to a given base price based on the specified quantity thresholds.
 */
public class QuantityDiscountPolicy implements DiscountPolicy {

  private final NavigableMap<Integer, BigDecimal> quantityThresholds;

  public QuantityDiscountPolicy(NavigableMap<Integer, BigDecimal> quantityThresholds) {
    this.quantityThresholds = quantityThresholds;
  }

  @Override
  public BigDecimal apply(BigDecimal basePrice, int quantity) {
    BigDecimal discountRate =
        quantityThresholds.floorEntry(quantity) != null
            ? quantityThresholds.floorEntry(quantity).getValue()
            : BigDecimal.ZERO;

    return basePrice.subtract(basePrice.multiply(discountRate));
  }
}
