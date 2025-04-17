package com.zen.ala.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/** Product domain model */
@Getter
@Setter
@AllArgsConstructor
public class Product {

  private UUID id;
  private String name;
  private BigDecimal price;
}
