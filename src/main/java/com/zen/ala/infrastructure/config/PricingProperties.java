package com.zen.ala.infrastructure.config;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "pricing.discount")
public class PricingProperties {
  private BigDecimal percentage;
  private Map<Integer, BigDecimal> thresholds;
}
