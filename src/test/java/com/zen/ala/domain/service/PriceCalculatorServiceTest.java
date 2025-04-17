package com.zen.ala.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.zen.ala.domain.service.discount.DiscountStrategy;
import com.zen.ala.domain.service.discount.PercentageDiscount;
import com.zen.ala.domain.service.discount.QuantityDiscount;
import com.zen.ala.domain.service.discount.dict.DiscountPolicy;
import com.zen.ala.domain.service.discount.dict.DiscountType;
import java.math.BigDecimal;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link PriceCalculatorService}.
 *
 * <p>This test class is responsible for testing the functionality of the {@link
 * PriceCalculatorService} class. It includes various test cases to ensure that the price
 * calculation logic works as expected with different discount strategies and policies.
 */
public class PriceCalculatorServiceTest {

  PriceCalculatorService service;

  @BeforeEach
  void setup() {
    NavigableMap<Integer, BigDecimal> quantityThresholds = new TreeMap<>();
    quantityThresholds.put(10, BigDecimal.valueOf(0.05)); // 5%
    quantityThresholds.put(20, BigDecimal.valueOf(0.10)); // 10%
    quantityThresholds.put(50, BigDecimal.valueOf(0.15)); // 15%

    QuantityDiscount quantityDiscount = new QuantityDiscount(quantityThresholds);
    PercentageDiscount percentageDiscount = new PercentageDiscount(BigDecimal.valueOf(0.10)); // 10%

    service = new PriceCalculatorService(List.of(quantityDiscount, percentageDiscount));
  }

  @Test
  void shouldApplyQuantityDiscountOnly_Cumulative() {
    DiscountStrategy strategy =
        new DiscountStrategy(DiscountType.QUANTITY, DiscountPolicy.CUMULATIVE);

    // for quantity = 10 (5% discount)
    BigDecimal result10 = service.calculatePrice(BigDecimal.valueOf(100), 10, strategy);
    assertThat(result10).isEqualByComparingTo(BigDecimal.valueOf(950));

    // for quantity = 20 (10% discount)
    BigDecimal result20 = service.calculatePrice(BigDecimal.valueOf(100), 20, strategy);
    assertThat(result20).isEqualByComparingTo(BigDecimal.valueOf(1800));

    // for quantity = 50 (15% discount)
    BigDecimal result50 = service.calculatePrice(BigDecimal.valueOf(100), 50, strategy);
    assertThat(result50).isEqualByComparingTo(BigDecimal.valueOf(4250));
  }

  @Test
  void shouldApplyQuantityDiscountOnly_Highest() {
    DiscountStrategy strategy = new DiscountStrategy(DiscountType.QUANTITY, DiscountPolicy.HIGHEST);

    // for quantity = 10 (5% discount)
    BigDecimal result10 = service.calculatePrice(BigDecimal.valueOf(100), 10, strategy);
    assertThat(result10).isEqualByComparingTo(BigDecimal.valueOf(950));

    // for quantity = 20 (10% discount)
    BigDecimal result20 = service.calculatePrice(BigDecimal.valueOf(100), 20, strategy);
    assertThat(result20).isEqualByComparingTo(BigDecimal.valueOf(1800));

    // for quantity = 50 (15% discount)
    BigDecimal result50 = service.calculatePrice(BigDecimal.valueOf(100), 50, strategy);
    assertThat(result50).isEqualByComparingTo(BigDecimal.valueOf(4250));
  }

  @Test
  void shouldApplyPercentageDiscountOnly_Cumulative() {
    DiscountStrategy strategy =
        new DiscountStrategy(DiscountType.PERCENTAGE, DiscountPolicy.CUMULATIVE);

    BigDecimal result = service.calculatePrice(BigDecimal.valueOf(100), 10, strategy);

    // 100 * 10 = 1000
    // -10% percentage discount => 900
    assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(900));
  }

  @Test
  void shouldApplyPercentageDiscountOnly_Highest() {
    DiscountStrategy strategy =
        new DiscountStrategy(DiscountType.PERCENTAGE, DiscountPolicy.HIGHEST);

    BigDecimal result = service.calculatePrice(BigDecimal.valueOf(100), 20, strategy);

    // 100 * 20 = 2000
    // -10% percentage discount => 1800
    assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(1800));
  }

  @Test
  void shouldApplyBothDiscounts_Cumulative() {
    DiscountStrategy strategy = new DiscountStrategy(DiscountType.BOTH, DiscountPolicy.CUMULATIVE);

    BigDecimal result = service.calculatePrice(BigDecimal.valueOf(100), 50, strategy);

    // 100 * 50 = 5000
    // -15% quantity discount => 4250
    // -10% percentage discount => 3825
    assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(3825));
  }

  @Test
  void shouldApplyBestDiscount_Both_Highest() {
    DiscountStrategy strategy = new DiscountStrategy(DiscountType.BOTH, DiscountPolicy.HIGHEST);

    BigDecimal result = service.calculatePrice(BigDecimal.valueOf(100), 50, strategy);

    // 100 * 50 = 5000
    // -15% quantity discount => 4250
    // -10% percentage discount => 4500
    // wybieramy 4250
    assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(4250));
  }
}
