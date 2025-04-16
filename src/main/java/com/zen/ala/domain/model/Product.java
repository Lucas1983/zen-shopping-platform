package com.zen.ala.domain.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Product domain model */
@Getter
@RequiredArgsConstructor
public class Product {

  private final UUID id;
  private final String name;
  private final BigDecimal price;
}
