package com.zen.ala.domain.service.discount.dict;

/**
 * Enum representing different types of discounts.
 *
 * <p>This enum defines the types of discounts that can be applied to products. The three types are:
 * <lu>
 * <li>QUANTITY: Discounts based on the quantity of items purchased.
 * <li>PERCENTAGE: Discounts based on a percentage of the total price.
 * <li>BOTH: A combination of both quantity and percentage discounts. </lu>
 */
public enum DiscountType {
  QUANTITY,
  PERCENTAGE,
  BOTH
}
