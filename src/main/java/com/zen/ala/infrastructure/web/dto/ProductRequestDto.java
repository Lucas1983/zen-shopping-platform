package com.zen.ala.infrastructure.web.dto;

import java.math.BigDecimal;

/**
 * ProductRequestDto is a data transfer object that represents a product request.
 *
 * @param name the name of the product
 * @param price the price of the product
 */
public record ProductRequestDto(String name, BigDecimal price) {}
