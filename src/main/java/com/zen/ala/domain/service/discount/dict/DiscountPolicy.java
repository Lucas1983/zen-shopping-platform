package com.zen.ala.domain.service.discount.dict;

/**
 * Enum representing different discount policies.
 *
 * <p>This enum defines the strategies for applying discounts to products. The two policies are:
 * <lu>
 * <li>CUMULATIVE: Discounts are applied cumulatively, meaning that multiple discounts can be
 *     combined.
 * <li>HIGHEST: Only the highest discount is applied, ignoring any other discounts. </lu>
 */
public enum DiscountPolicy {
  CUMULATIVE,
  HIGHEST
}
