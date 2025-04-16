package com.zen.ala.infrastructure.web.controller;

import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.port.in.ProductRetriever;
import com.zen.ala.domain.port.out.ProductLoader;
import com.zen.ala.infrastructure.web.dto.ProductRequestDto;
import com.zen.ala.infrastructure.web.dto.ProductResponseDto;
import com.zen.ala.infrastructure.web.mapper.ProductRequestDtoMapper;
import com.zen.ala.infrastructure.web.mapper.ProductResponseDtoMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** ProductController is responsible for handling HTTP requests related to products. */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductRetriever productRetriever;
  private final ProductLoader productLoader;
  private final ProductRequestDtoMapper productRequestDtoMapper;
  private final ProductResponseDtoMapper productResponseDtoMapper;

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getById(@PathVariable UUID id) {
    Product product = productRetriever.getProductById(id);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> getAll() {
    List<ProductResponseDto> productDtos =
        productRetriever.getAllProducts().stream()
            .map(productResponseDtoMapper::toDto)
            .collect(Collectors.toList());
    return ResponseEntity.ok(productDtos);
  }

  @PostMapping
  public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto request) {
    Product product = productRequestDtoMapper.toDomain(request);
    productLoader.saveProduct(product);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDto> updateProduct(
      @PathVariable UUID id, @RequestBody ProductRequestDto request) {
    Product product = productRequestDtoMapper.toDomain(id, request);
    productLoader.saveProduct(product);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
    productLoader.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
