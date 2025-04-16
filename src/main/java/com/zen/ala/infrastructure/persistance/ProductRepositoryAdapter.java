package com.zen.ala.infrastructure.persistance;

import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.port.out.ProductLoader;
import com.zen.ala.infrastructure.persistance.mapper.ProductEntityMapper;
import com.zen.ala.infrastructure.persistance.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * ProductRepositoryAdapter is an adapter that implements the ProductLoader interface, and uses
 * ProductRepository for data access operations.
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductLoader {

  private final ProductRepository productRepository;
  private final ProductEntityMapper productEntityMapper;

  @Override
  public Product findProductById(UUID id) {
    return productRepository
        .findById(id)
        .map(productEntityMapper::toDomain)
        .orElseThrow(() -> new IllegalArgumentException("Product not found"));
  }

  @Override
  public List<Product> findAllProducts() {
    return productRepository.findAll().stream().map(productEntityMapper::toDomain).toList();
  }

  @Override
  public void saveProduct(Product product) {
    productRepository.save(productEntityMapper.toEntity(product));
  }

  @Override
  public void deleteProduct(UUID id) {
    productRepository.deleteById(id);
  }
}
