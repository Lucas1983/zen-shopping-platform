package com.zen.ala.infrastructure.persistance;

import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.port.out.ProductOutboundPort;
import com.zen.ala.infrastructure.persistance.mapper.ProductEntityMapper;
import com.zen.ala.infrastructure.persistance.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adapter class that implements the ProductOutboundPort interface and interacts with the
 * ProductRepository to perform CRUD operations on Product entities.
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductOutboundPort {

  private final ProductRepository productRepository;
  private final ProductEntityMapper productEntityMapper;

  /**
   * Finds a product by its ID.
   *
   * @param id the UUID of the product
   * @return the Product object if found
   * @throws IllegalArgumentException if the product is not found
   */
  @Override
  public Product findProductById(UUID id) {
    return productRepository
        .findById(id)
        .map(productEntityMapper::toDomain)
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
  }

  /**
   * Finds all products.
   *
   * @return a list of Product objects
   */
  @Override
  public List<Product> findAllProducts() {
    return productRepository.findAll().stream().map(productEntityMapper::toDomain).toList();
  }

  /**
   * Saves a new product.
   *
   * @param product the Product object to save
   * @return the saved Product object
   */
  @Override
  public Product saveProduct(Product product) {
    return Optional.of(productRepository.save(productEntityMapper.toEntity(product)))
        .map(productEntityMapper::toDomain)
        .orElseThrow(() -> new IllegalArgumentException("Product not saved"));
  }

  /**
   * Updates an existing product.
   *
   * @param product the Product object to update
   * @return the updated Product object
   */
  @Override
  public Product updateProduct(Product product) {
    return Optional.of(productRepository.save(productEntityMapper.toEntity(product)))
        .map(productEntityMapper::toDomain)
        .orElseThrow(() -> new IllegalArgumentException("Product not updated"));
  }

  /**
   * Deletes a product by its ID.
   *
   * @param id the UUID of the product to delete
   */
  @Override
  public void deleteProduct(UUID id) {
    productRepository.deleteById(id);
  }
}
