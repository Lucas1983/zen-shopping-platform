package com.zen.ala.domain.service.discount;

import com.zen.ala.domain.error.InvalidDiscountStrategyException;
import com.zen.ala.domain.service.discount.dict.DiscountPolicy;
import com.zen.ala.domain.service.discount.dict.DiscountType;
import lombok.Data;

/**
 * Represents a discount strategy that combines a discount type and a discount policy.
 *
 * <p>This class is used to define how discounts are applied to products. It includes the type of
 * discount (e.g., quantity, percentage, both) and the policy for applying the discount (e.g.,
 * cumulative, highest).
 */
@Data
public class DiscountStrategy {

  private DiscountType discountType;

  private DiscountPolicy discountPolicy;

  public DiscountStrategy(DiscountType discountType, DiscountPolicy discountPolicy) {
    // TODO: Implement validation logic
    // validate(discountType, discountPolicy);
    this.discountType = discountType;
    this.discountPolicy = discountPolicy;
  }

  private void validate(DiscountType discountType, DiscountPolicy discountPolicy) {
    throw new InvalidDiscountStrategyException(
        String.format(
            "Invalid discount strategy: combination of DiscountType '%s' and DiscountPolicy '%s' not allowed",
            discountType, discountPolicy));
  }
}
