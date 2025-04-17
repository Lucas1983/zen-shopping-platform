package com.zen.ala.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zen.ala.application.service.ProductRetrievalService;
import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.service.PriceCalculatorService;
import com.zen.ala.infrastructure.persistance.ProductRepositoryAdapter;
import com.zen.ala.infrastructure.web.controller.ProductController;
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
@Import({ProductRequestDtoMapperImpl.class, ProductResponseDtoMapperImpl.class})
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ProductRetrievalService productRetrievalService;
  @MockitoBean private ProductRepositoryAdapter productRepositoryAdapter;
  @MockitoBean private PriceCalculatorService priceCalculatorService;
  @MockitoSpyBean private ProductRequestDtoMapper productRequestDtoMapper;
  @MockitoSpyBean private ProductResponseDtoMapper productResponseDtoMapper;

  @Test
  void shouldReturnCalculatedPrice() throws Exception {
    // given
    UUID productId = UUID.randomUUID();
    Product product = new Product(productId, "Test Product", BigDecimal.valueOf(100));

    when(productRetrievalService.getProductById(eq(productId))).thenReturn(product);
    when(priceCalculatorService.calculatePrice(any(), eq(10))).thenReturn(BigDecimal.valueOf(500));

    // when + then
    mockMvc
        .perform(
            get("/products/" + productId + "/calculate-price")
                .param("quantity", "10")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(BigDecimal.valueOf(500).toString()));
  }

  @Test
  void shouldCreateNewProduct() throws Exception {
    // given
    // when + then
    mockMvc
        .perform(
            post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "name": "Test Product",
                            "price": 100
                        }
                        """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Product"))
        .andExpect(jsonPath("$.price").value(100));
  }

  @Test
  void shouldReturnBadRequestWhenMissingName() throws Exception {
    mockMvc
        .perform(
            post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
										{
										    "price": 100
										}
										"""))
        .andExpect(status().isBadRequest());
  }

  //  @Test
  //  void shouldReturn404WhenProductNotFound() throws Exception {
  //    // given
  //    UUID unknownId = UUID.randomUUID();
  //
  //    Mockito.when(productRetrievalService.getProductById(eq(unknownId)))
  //        .thenThrow(new RuntimeException("Product not found"));
  //
  //    // when + then
  //    mockMvc.perform(get("/products/" + unknownId)).andExpect(status().isNotFound());
  //  }

  @Test
  void shouldReturnSingleProduct() throws Exception {
    // given
    UUID productId = UUID.randomUUID();
    Product product = new Product(productId, "Test Product", BigDecimal.valueOf(100));

    // when
    when(productRetrievalService.getProductById(eq(productId))).thenReturn(product);

    // then
    mockMvc
        .perform(get("/products/" + productId).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Test Product"))
        .andExpect(jsonPath("$.price").value(100));
  }

  @Test
  void shouldReturnListOfProducts() throws Exception {
    // given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();

    Product product1 = new Product(id1, "Test Product 1", BigDecimal.valueOf(100));
    Product product2 = new Product(id2, "Test Product 2", BigDecimal.valueOf(200));

    // when
    when(productRetrievalService.getAllProducts()).thenReturn(List.of(product1, product2));

    // then
    mockMvc
        .perform(get("/products").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Test Product 1"))
        .andExpect(jsonPath("$[1].name").value("Test Product 2"));
  }
}
