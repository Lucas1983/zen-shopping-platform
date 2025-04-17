package com.zen.ala.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * ProductResponseDto is a data transfer object that represents a product response.
 *
 * @param id the unique identifier of the product
 * @param name the name of the product
 * @param price the price of the product
 */
public record ProductResponseDto(UUID id, String name, BigDecimal price) {}
