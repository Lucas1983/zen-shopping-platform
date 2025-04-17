package com.zen.ala;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zen.ala.application.service.ProductService;
import com.zen.ala.domain.model.Product;
import com.zen.ala.infrastructure.persistance.ProductRepositoryAdapter;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductIntegrationTest {

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>()
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @Autowired private MockMvc mockMvc;

  @Autowired private ProductRepositoryAdapter productRepositoryAdapter;

  @Autowired private ProductService productService;

  @DynamicPropertySource
  static void configure(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
  }

  @Test
  void shouldCreateAndFetchProduct() throws Exception {
    var id = UUID.randomUUID().toString();
    // given:
    String createResponse =
        mockMvc
            .perform(
                post("/products")
                    .contentType("application/json")
                    .content(
                        """
		                            {
		                        		"id": "%s",
		                                "name": "Product",
		                                "price": 100
		                            }
		                        """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product"))
            .andExpect(jsonPath("$.price").value(100))
            .andReturn()
            .getResponse()
            .getContentAsString();

    id = createResponse.split("\"id\":\"")[1].split("\"")[0];

    // then
    mockMvc
        .perform(get("/products/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Product"))
        .andExpect(jsonPath("$.price").value(100));
  }

  @BeforeEach
  void setup() {}

  @Test
  void shouldCalculatePriceSuccessfully() throws Exception {

    // given
    int quantity = 10;
    String discountType = "BOTH";
    String discountPolicy = "CUMULATIVE";

    Product product = new Product(UUID.randomUUID(), "Test Product", BigDecimal.valueOf(100));
    Product saved = productRepositoryAdapter.saveProduct(product);
    var productId = saved.getId(); // when

    // then
    mockMvc
        .perform(
            get("/products/" + productId + "/calculate-price")
                .param("quantity", String.valueOf(quantity))
                .param("discountType", discountType)
                .param("discountPolicy", discountPolicy)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("4590"));
  }
}
