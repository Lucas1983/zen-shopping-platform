package com.zen.ala.domain.service.discount;

import java.math.BigDecimal;

/**
 * Interface for discount policies.
 *
 * <p>This interface defines a method for applying a discount to a base price based on the quantity
 * of items purchased.
 *
 * <p>Implementations of this interface can provide different discount strategies, such as
 * percentage-based discounts or quantity-based discounts.
 */
public interface DiscountPolicy {
  BigDecimal apply(BigDecimal basePrice, int quantity);
}
