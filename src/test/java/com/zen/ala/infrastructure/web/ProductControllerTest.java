package com.zen.ala.infrastructure.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zen.ala.application.service.ProductService;
import com.zen.ala.domain.error.InvalidDiscountStrategyException;
import com.zen.ala.domain.error.ProductNotFoundException;
import com.zen.ala.domain.model.Product;
import com.zen.ala.infrastructure.persistance.ProductRepositoryAdapter;
import com.zen.ala.infrastructure.web.controller.ProductController;
import com.zen.ala.infrastructure.web.dto.ProductResponseDto;
import com.zen.ala.infrastructure.web.exception.ProductExceptionHandler;
import com.zen.ala.infrastructure.web.mapper.ProductRequestDtoMapper;
import com.zen.ala.infrastructure.web.mapper.ProductRequestDtoMapperImpl;
import com.zen.ala.infrastructure.web.mapper.ProductResponseDtoMapper;
import com.zen.ala.infrastructure.web.mapper.ProductResponseDtoMapperImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@Import({
  ProductRequestDtoMapperImpl.class,
  ProductResponseDtoMapperImpl.class,
  ProductExceptionHandler.class
})
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ProductService productService;
  @MockitoBean private ProductRepositoryAdapter productRepositoryAdapter;
  @MockitoSpyBean private ProductRequestDtoMapper productRequestDtoMapper;
  @MockitoSpyBean private ProductResponseDtoMapper productResponseDtoMapper;
  @MockitoSpyBean private ProductExceptionHandler productExceptionHandler;

  @Test
  void shouldReturnSingleProduct() throws Exception {
    // given
    UUID productId = UUID.randomUUID();
    Product product = new Product(productId, "Product", BigDecimal.valueOf(100));

    // when
    when(productService.getProductById(eq(productId))).thenReturn(product);

    // then
    mockMvc
        .perform(get("/products/" + productId).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Product"))
        .andExpect(jsonPath("$.price").value(100));
  }

  @Test
  void shouldReturnListOfProducts() throws Exception {
    // given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    Product product1 = new Product(id1, "Product 1", BigDecimal.valueOf(100));
    Product product2 = new Product(id2, "Product 2", BigDecimal.valueOf(200));

    // when
    when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

    // then
    mockMvc
        .perform(get("/products").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Product 1"))
        .andExpect(jsonPath("$[1].name").value("Product 2"));
  }

  @Test
  void shouldCreateNewProduct() throws Exception {
    // given
    Product product = new Product(UUID.randomUUID(), "Product", BigDecimal.valueOf(100));
    // when
    when(productService.saveProduct(any())).thenReturn(product);
    // then
    mockMvc
        .perform(
            post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
											{
												"name": "Product",
												"price": 100
											}
											"""))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Product"))
        .andExpect(jsonPath("$.price").value(100));
  }

  @Test
  void shouldUpdateProduct() throws Exception {
    // given
    UUID productId = UUID.randomUUID();
    Product existingProduct = new Product(productId, "Old Product", BigDecimal.valueOf(100));

    // when
    when(productService.getProductById(eq(productId))).thenReturn(existingProduct);
    when(productRepositoryAdapter.saveProduct(any()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(productResponseDtoMapper.toDto(any()))
        .thenReturn(new ProductResponseDto(productId, "Updated Product", BigDecimal.valueOf(150)));

    //  then
    mockMvc
        .perform(
            put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                    "name": "Updated Product",
                    "price": 150
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Product"))
        .andExpect(jsonPath("$.price").value(150));
  }

  @Test
  void shouldDeleteProduct() throws Exception {
    // given
    UUID productId = UUID.randomUUID();

    // when
    doNothing().when(productRepositoryAdapter).deleteProduct(productId);

    //  then
    mockMvc.perform(delete("/products/" + productId)).andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnCalculatedPrice() throws Exception {
    // given
    UUID productId = UUID.randomUUID();
    Product product = new Product(productId, "Product", BigDecimal.valueOf(100));

    when(productService.getProductById(any())).thenReturn(product);
    when(productService.calculateDiscountedPrice(any(), eq(1), any(), any()))
        .thenReturn(BigDecimal.valueOf(500));

    // when + then
    mockMvc
        .perform(
            get("/products/" + productId + "/calculate-price")
                .param("quantity", "1")
                .param("discountType", "QUANTITY")
                .param("discountPolicy", "CUMULATIVE")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(BigDecimal.valueOf(500).toString()));
  }

  @Test
  void shouldReturn404WhenProductNotFound() throws Exception {
    // given
    UUID productId = UUID.randomUUID();
    when(productService.getProductById(productId))
        .thenThrow(new ProductNotFoundException(productId));

    // when + then
    mockMvc
        .perform(get("/products/" + productId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Product with id " + productId + " not found."));
  }

  @Test
  void shouldReturn400WhenInvalidDiscountStrategy() throws Exception {
    // given
    UUID productId = UUID.randomUUID();

    // when
    when(productService.getProductById(productId))
        .thenReturn(new Product(productId, "Test", BigDecimal.valueOf(100)));
    when(productService.calculateDiscountedPrice(
            any(BigDecimal.class), anyInt(), anyString(), anyString()))
        .thenThrow(
            new InvalidDiscountStrategyException(
                "DiscountType and/or DiscountPolicy cannot be empty"));

    //  then
    mockMvc
        .perform(
            get("/products/" + productId + "/calculate-price")
                .param("quantity", "10")
                .param("discountType", "")
                .param("discountPolicy", ""))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(
            jsonPath("$.message").value("DiscountType and/or DiscountPolicy cannot be empty"));
  }

  @Test
  void shouldReturn500WhenUnexpectedError() throws Exception {
    // given
    UUID productId = UUID.randomUUID();
    when(productService.getProductById(productId))
        .thenThrow(new RuntimeException("Something went wrong"));

    // when + then
    mockMvc
        .perform(get("/products/" + productId))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.message").value("Unexpected error occurred."));
  }
}
