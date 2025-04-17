package com.zen.ala.domain.service;

import com.zen.ala.domain.service.discount.Discount;
import com.zen.ala.domain.service.discount.DiscountStrategy;
import com.zen.ala.domain.service.discount.PercentageDiscount;
import com.zen.ala.domain.service.discount.QuantityDiscount;
import com.zen.ala.domain.service.discount.dict.DiscountType;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Service for calculating prices with discounts.
 *
 * <p>This service provides methods to calculate the final price of a product based on its unit
 * price, quantity, and applicable discounts. It supports different discount strategies, including
 * cumulative and highest discount policies.
 */
@RequiredArgsConstructor
public class PriceCalculatorService {

  private final List<Discount> discounts;

  /**
   * Calculates the final price based on the unit price, quantity, and discount strategy.
   *
   * @param unitPrice the unit price of the product
   * @param quantity the quantity of products
   * @param discountStrategy the discount strategy to apply
   * @return the final price after applying discounts
   */
  public BigDecimal calculatePrice(
      BigDecimal unitPrice, int quantity, DiscountStrategy discountStrategy) {

    BigDecimal originalTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

    List<Discount> selectedDiscounts = selectDiscounts(discountStrategy.getDiscountType());

    return switch (discountStrategy.getDiscountPolicy()) {
      case CUMULATIVE -> calculateCumulativePrice(quantity, originalTotal, selectedDiscounts);
      case HIGHEST -> calculateHighestPrice(quantity, originalTotal, selectedDiscounts);
    };
  }

  private List<Discount> selectDiscounts(DiscountType discountType) {
    return switch (discountType) {
      case QUANTITY ->
          discounts.stream().filter(policy -> policy instanceof QuantityDiscount).toList();
      case PERCENTAGE ->
          discounts.stream().filter(policy -> policy instanceof PercentageDiscount).toList();
      case BOTH -> discounts;
    };
  }

  private BigDecimal calculateCumulativePrice(
      int quantity, BigDecimal total, List<Discount> selectedDiscounts) {

    BigDecimal result = total;
    for (Discount discount : selectedDiscounts) {
      result = discount.apply(result, quantity);
    }
    return result;
  }

  private BigDecimal calculateHighestPrice(
      int quantity, BigDecimal total, List<Discount> selectedDiscounts) {

    return selectedDiscounts.stream()
        .map(discount -> discount.apply(total, quantity))
        .min(BigDecimal::compareTo)
        .orElse(total);
  }
}
