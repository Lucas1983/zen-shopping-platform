package com.zen.ala.domain.service;

import com.zen.ala.domain.service.discount.DiscountPolicy;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service for calculating the total price of items based on unit price and quantity.
 *
 * <p>This service applies various discount policies to the total price based on the specified
 * quantity.
 */
public class PriceCalculatorService {

  private final List<DiscountPolicy> discountPolicies;

  public PriceCalculatorService(List<DiscountPolicy> discountPolicies) {
    this.discountPolicies = discountPolicies;
  }

  public BigDecimal calculatePrice(BigDecimal unitPrice, int quantity) {
    BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(quantity));

    for (DiscountPolicy policy : discountPolicies) {
      total = policy.apply(total, quantity);
    }

    return total;
  }
}
