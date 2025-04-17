package com.zen.ala.infrastructure.config;

import com.zen.ala.domain.service.PriceCalculatorService;
import com.zen.ala.domain.service.discount.PercentageDiscount;
import com.zen.ala.domain.service.discount.QuantityDiscount;
import java.math.BigDecimal;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for pricing-related beans.
 *
 * <p>This class defines beans for different discount policies and the price calculator service. It
 * uses Spring's dependency injection to manage the lifecycle of these beans.
 */
@Configuration
public class PricingConfig {

  @Bean
  public QuantityDiscount quantityDiscountPolicy(PricingProperties props) {
    NavigableMap<Integer, BigDecimal> thresholds = new TreeMap<>(props.getQuantityThresholds());
    return new QuantityDiscount(thresholds);
  }

  @Bean
  public PercentageDiscount percentageDiscountPolicy(PricingProperties props) {
    return new PercentageDiscount(props.getPercentage());
  }

  @Bean
  public PriceCalculatorService priceCalculatorService(
      QuantityDiscount quantityDiscount, PercentageDiscount percentageDiscount) {
    return new PriceCalculatorService(List.of(percentageDiscount, quantityDiscount));
  }
}
