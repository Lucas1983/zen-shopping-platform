package com.zen.ala;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.zen.ala.application.service.ProductService;
import com.zen.ala.infrastructure.persistance.ProductRepositoryAdapter;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

  public static final String PRODUCTS = "/v1/products";

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
    String requestBody =
        String.format(
            """
			  {
				  "id": "%s",
				  "name": "Product",
				  "price": 100
			  }
			  """,
            id);

    // given
    String createResponse =
        mockMvc
            .perform(post(PRODUCTS).contentType("application/json").content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product"))
            .andExpect(jsonPath("$.price").value(100))
            .andReturn()
            .getResponse()
            .getContentAsString();

    id = createResponse.split("\"id\":\"")[1].split("\"")[0];

    // then
    mockMvc
        .perform(get(PRODUCTS + "/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Product"))
        .andExpect(jsonPath("$.price").value(100));
  }
}
