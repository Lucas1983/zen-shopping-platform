package com.zen.ala.infrastructure.web.controller;

import com.zen.ala.domain.error.InvalidDiscountStrategyException;
import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.port.in.ProductInboundPort;
import com.zen.ala.infrastructure.web.dto.ProductRequestDto;
import com.zen.ala.infrastructure.web.dto.ProductResponseDto;
import com.zen.ala.infrastructure.web.mapper.ProductRequestDtoMapper;
import com.zen.ala.infrastructure.web.mapper.ProductResponseDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductInboundPort productInboundPort;
  private final ProductRequestDtoMapper productRequestDtoMapper;
  private final ProductResponseDtoMapper productResponseDtoMapper;

  @Operation(
      summary = "Get product by ID",
      description = "Retrieves a single product by its UUID.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Product found",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getById(
      @Parameter(
              description = "UUID of the product",
              example = "08c0a9b8-79c6-4aac-95be-d768f74abf9d")
          @PathVariable
          UUID id) {
    Product product = productInboundPort.getProductById(id);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  @Operation(
      summary = "Get all products",
      description = "Retrieves all products available in the system.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "List of products retrieved",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
      })
  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> getAll() {
    List<ProductResponseDto> productDtos =
        productInboundPort.getAllProducts().stream()
            .map(productResponseDtoMapper::toDto)
            .collect(Collectors.toList());
    return ResponseEntity.ok(productDtos);
  }

  @Operation(
      summary = "Create a new product",
      description = "Creates a new product and returns the created product details.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Product details",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = ProductRequestDto.class),
                      examples =
                          @ExampleObject(
                              value =
                                  """
                                            {
                                              "name": "New Product",
                                              "price": 100.0
                                            }
                                            """))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
      })
  @PostMapping
  public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto request) {
    Product product = productRequestDtoMapper.toDomain(request);
    product = productInboundPort.saveProduct(product);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  @Operation(
      summary = "Update a product",
      description = "Updates an existing product based on its UUID.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Updated product details",
              required = true,
              content =
                  @Content(
                      schema = @Schema(implementation = ProductRequestDto.class),
                      examples =
                          @ExampleObject(
                              value =
                                  """
                                            {
                                              "name": "Updated Product",
                                              "price": 120.0
                                            }
                                            """))),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDto> updateProduct(
      @Parameter(
              description = "UUID of the product to update",
              example = "08c0a9b8-79c6-4aac-95be-d768f74abf9d")
          @PathVariable
          UUID id,
      @RequestBody ProductRequestDto request) {
    Product product = productRequestDtoMapper.toDomain(id, request);
    productInboundPort.saveProduct(product);
    return ResponseEntity.ok(productResponseDtoMapper.toDto(product));
  }

  @Operation(
      summary = "Delete a product",
      description = "Deletes a product by its UUID.",
      responses = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(
      @Parameter(
              description = "UUID of the product to delete",
              example = "08c0a9b8-79c6-4aac-95be-d768f74abf9d")
          @PathVariable
          UUID id) {
    productInboundPort.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(
      summary = "Calculate product price with discounts",
      description =
          "Calculates the total price for a given quantity of a product applying selected discount strategy.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Price calculated successfully",
            content = @Content(schema = @Schema(implementation = BigDecimal.class))),
        @ApiResponse(responseCode = "400", description = "Invalid discount strategy"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @GetMapping("/{id}/calculate-price")
  public ResponseEntity<BigDecimal> calculatePrice(
      @Parameter(
              description = "UUID of the product",
              example = "08c0a9b8-79c6-4aac-95be-d768f74abf9d")
          @PathVariable
          UUID id,
      @Parameter(description = "Quantity of products", example = "10") @RequestParam int quantity,
      @Parameter(description = "Discount type (QUANTITY, PERCENTAGE, BOTH)", example = "BOTH")
          @RequestParam
          String discountType,
      @Parameter(description = "Discount policy (CUMULATIVE, HIGHEST)", example = "CUMULATIVE")
          @RequestParam
          String discountPolicy) {

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
