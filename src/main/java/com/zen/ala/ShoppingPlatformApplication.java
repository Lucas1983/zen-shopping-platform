package com.zen.ala;

import com.zen.ala.infrastructure.config.PricingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(PricingProperties.class)
public class ShoppingPlatformApplication {

  public static void main(String[] args) {
    SpringApplication.run(ShoppingPlatformApplication.class, args);
  }
}
