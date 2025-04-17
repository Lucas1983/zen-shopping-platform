package com.zen.ala.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zen.ala.domain.service.discount.PercentageDiscountPolicy;
import com.zen.ala.domain.service.discount.QuantityDiscountPolicy;
import java.math.BigDecimal;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.junit.jupiter.api.Test;

public class PriceCalculatorServiceTest {

  @Test
  void shouldApplyOnlyPercentageDiscountPolicy() {

    // given
    PercentageDiscountPolicy percentagePolicy =
        new PercentageDiscountPolicy(new BigDecimal("0.10")); // 10%
    PriceCalculatorService service = new PriceCalculatorService(List.of(percentagePolicy));

    // when
    BigDecimal result = service.calculatePrice(BigDecimal.valueOf(100), 10);

    // then
    assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(900));
  }

  @Test
  void shouldApplyOnlyQuantityDiscountPolicy() {

    // given
    NavigableMap<Integer, BigDecimal> thresholds = new TreeMap<>();
    thresholds.put(10, new BigDecimal("0.05")); // 5%
    thresholds.put(20, new BigDecimal("0.10")); // 10%
    thresholds.put(50, new BigDecimal("0.15")); // 15%

    QuantityDiscountPolicy quantityPolicy = new QuantityDiscountPolicy(thresholds);
    PriceCalculatorService service = new PriceCalculatorService(List.of(quantityPolicy));

    // when 0%
    BigDecimal result1 = service.calculatePrice(BigDecimal.valueOf(100), 1);
    // then
    assertThat(result1).isEqualByComparingTo(BigDecimal.valueOf(100));

    // when 5%
    BigDecimal result2 = service.calculatePrice(BigDecimal.valueOf(100), 10);
    // then
    assertThat(result2).isEqualByComparingTo(BigDecimal.valueOf(950));

    // when 10%
    BigDecimal result3 = service.calculatePrice(BigDecimal.valueOf(100), 20);
    // then
    assertThat(result3).isEqualByComparingTo(BigDecimal.valueOf(1800));

    // when 15%
    BigDecimal result4 = service.calculatePrice(BigDecimal.valueOf(100), 50);
    // then
    assertThat(result4).isEqualByComparingTo(BigDecimal.valueOf(4250));
  }

  @Test
  void shouldApplyBothDiscountPolicies() {

    // given
    NavigableMap<Integer, BigDecimal> thresholds = new TreeMap<>();
    thresholds.put(10, new BigDecimal("0.05")); // 5%
    thresholds.put(20, new BigDecimal("0.10")); // 10%
    thresholds.put(50, new BigDecimal("0.15")); // 15%

    QuantityDiscountPolicy quantityPolicy = new QuantityDiscountPolicy(thresholds);
    PercentageDiscountPolicy percentagePolicy =
        new PercentageDiscountPolicy(new BigDecimal("0.10")); // 10%

    PriceCalculatorService service =
        new PriceCalculatorService(List.of(quantityPolicy, percentagePolicy));

    // when
    BigDecimal result = service.calculatePrice(BigDecimal.valueOf(100), 20);

    // then
    assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(1620));
  }
}
