package com.zen.ala.infrastructure.persistance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.*;

/** ProductEntity represents a product in the database. */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

  @Id private UUID id;

  private String name;
  private BigDecimal price;
}
