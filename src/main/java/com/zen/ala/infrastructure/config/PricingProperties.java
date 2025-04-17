package com.zen.ala.infrastructure.config;

import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for pricing-related settings.
 *
 * <p>This class is used to bind properties from the application configuration file (e.g.,
 * application.yml) to Java objects. It allows for easy access and management of pricing-related
 * settings.
 */
@Data
@ConfigurationProperties(prefix = "pricing.discount")
public class PricingProperties {
  private BigDecimal percentage;
  private Map<Integer, BigDecimal> quantityThresholds;
}
