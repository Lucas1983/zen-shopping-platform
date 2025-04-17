package com.zen.ala.infrastructure.web.controller;

import com.zen.ala.domain.error.InvalidDiscountStrategyException;
import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.port.in.ProductInboundPort;
import com.zen.ala.infrastructure.web.dto.ProductRequestDto;
import com.zen.ala.infrastructure.web.dto.ProductResponseDto;
import com.zen.ala.infrastructure.web.mapper.ProductRequestDtoMapper;
import com.zen.ala.infrastructure.web.mapper.ProductResponseDtoMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** ProductController is responsible for handling HTTP requests related to products. */
@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductInboundPort productInboundPort;
  private final ProductRequestDtoMapper productRequestDtoMapper;
  private final ProductResponseDtoMapper productResponseDtoMapper;

  /**
   * Retrieves a product by its ID.
   *
   * @param id the UUID of the product
   * @return the product response DTO
   */
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getById(@PathVariable UUID id) {

    Product product = productInboundPort.getProductById(id);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  /**
   * Retrieves all products.
   *
   * @return a list of product response DTOs
   */
  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> getAll() {

    List<ProductResponseDto> productDtos =
        productInboundPort.getAllProducts().stream()
            .map(productResponseDtoMapper::toDto)
            .collect(Collectors.toList());
    return ResponseEntity.ok(productDtos);
  }

  /**
   * Creates a new product.
   *
   * @param request the product request DTO
   * @return the created product response DTO
   */
  @PostMapping
  public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto request) {

    Product product = productRequestDtoMapper.toDomain(request);
    product = productInboundPort.saveProduct(product);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  /**
   * Updates an existing product.
   *
   * @param id the UUID of the product
   * @param request the product request DTO
   * @return the updated product response DTO
   */
  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDto> updateProduct(
      @PathVariable UUID id, @RequestBody ProductRequestDto request) {

    Product product = productRequestDtoMapper.toDomain(id, request);
    productInboundPort.saveProduct(product);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  /**
   * Deletes a product by its ID.
   *
   * @param id the UUID of the product
   * @return a response entity with no content
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {

    productInboundPort.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Calculates the price of a product based on quantity and discount strategy.
   *
   * @param id the UUID of the product
   * @param quantity the quantity of the product
   * @param discountType the type of discount
   * @param discountPolicy the discount policy
   * @return the final price after applying the discount
   */
  @GetMapping("/{id}/calculate-price")
  public ResponseEntity<BigDecimal> calculatePrice(
      @PathVariable UUID id,
      @RequestParam int quantity,
      @RequestParam String discountType,
      @RequestParam String discountPolicy) {

    if (discountType == null || discountPolicy == null) {
      throw new InvalidDiscountStrategyException(
          "DiscountType and/or DiscountPolicy cannot be empty");
    }

    Product product = productInboundPort.getProductById(id);
    BigDecimal finalPrice =
        productInboundPort.calculateDiscountedPrice(
            product.getPrice(), quantity, discountType, discountPolicy);
    return ResponseEntity.ok(finalPrice);
  }
}
