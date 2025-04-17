package com.zen.ala.infrastructure.persistance;

import static org.assertj.core.api.Assertions.assertThat;

import com.zen.ala.domain.model.Product;
import com.zen.ala.infrastructure.persistance.mapper.ProductEntityMapperImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Test class for {@link ProductRepositoryAdapter}.
 *
 * <p>This test class is responsible for testing the functionality of the {@link
 * ProductRepositoryAdapter} class. It includes various test cases to ensure that the product
 * repository adapter works as expected with different product operations.
 */
@DataJpaTest
@Testcontainers
@Import({ProductRepositoryAdapter.class, ProductEntityMapperImpl.class})
class ProductRepositoryAdapterTest {

  @Autowired private ProductRepositoryAdapter adapter;

  @Test
  void shouldFindSingleProduct() {
    // given
    UUID id = UUID.randomUUID();
    Product product = new Product(id, "Product", BigDecimal.valueOf(100));

    // when
    adapter.saveProduct(product);
    Product found = adapter.findProductById(id);

    // then
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(id);
    assertThat(found.getName()).isEqualTo("Product");
    assertThat(found.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(100));
  }

  @Test
  void shouldFindAllProducts() {
    // given
    adapter.saveProduct(new Product(UUID.randomUUID(), "Product1", BigDecimal.valueOf(100)));
    adapter.saveProduct(new Product(UUID.randomUUID(), "Product2", BigDecimal.valueOf(200)));

    // when
    List<Product> products = adapter.findAllProducts();

    // then
    assertThat(products).hasSizeGreaterThanOrEqualTo(2);
  }

  @Test
  void shouldUpdateProduct() {
    // given
    UUID id = UUID.randomUUID();
    Product product = new Product(id, "Old Product", BigDecimal.valueOf(100));

    // when
    product = adapter.saveProduct(product);
    product.setName("Updated Product");
    product.setPrice(BigDecimal.valueOf(200));
    adapter.updateProduct(product);

    Product found = adapter.findProductById(id);

    // then
    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(id);
    assertThat(found.getName()).isEqualTo("Updated Product");
    assertThat(found.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(200));
  }

  @Test
  void shouldDeleteProduct() {
    // given
    UUID id = UUID.randomUUID();
    adapter.saveProduct(new Product(id, "Product", BigDecimal.valueOf(100)));

    // when
    adapter.deleteProduct(id);
    List<Product> products = adapter.findAllProducts();

    // then
    assertThat(products.stream().anyMatch(p -> p.getId().equals(id))).isFalse();
  }
}
