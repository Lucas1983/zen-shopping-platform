package com.zen.ala.infrastructure.config;

import com.zen.ala.domain.service.PriceCalculatorService;
import com.zen.ala.domain.service.discount.PercentageDiscountPolicy;
import com.zen.ala.domain.service.discount.QuantityDiscountPolicy;
import java.math.BigDecimal;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PricingConfig {

  @Bean
  public QuantityDiscountPolicy quantityDiscountPolicy(PricingProperties props) {
    NavigableMap<Integer, BigDecimal> thresholds = new TreeMap<>(props.getThresholds());
    return new QuantityDiscountPolicy(thresholds);
  }

  @Bean
  public PercentageDiscountPolicy percentageDiscountPolicy(PricingProperties props) {
    return new PercentageDiscountPolicy(props.getPercentage());
  }

  @Bean
  public PriceCalculatorService priceCalculatorService(
      QuantityDiscountPolicy quantityDiscountPolicy,
      PercentageDiscountPolicy percentageDiscountPolicy) {
    return new PriceCalculatorService(List.of(quantityDiscountPolicy, percentageDiscountPolicy));
  }
}
