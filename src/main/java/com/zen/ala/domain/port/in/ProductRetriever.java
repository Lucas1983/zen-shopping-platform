package com.zen.ala.domain.port.in;

import com.zen.ala.domain.model.Product;
import java.util.List;
import java.util.UUID;

/**
 * Product retriever port
 *
 * <p>Used to retrieve products from the application
 */
public interface ProductRetriever {

  /**
   * Get product by id
   *
   * @param id - product id
   * @return Product
   */
  Product getProductById(UUID id);

  /**
   * Get all products
   *
   * @return List<Product>
   */
  List<Product> getAllProducts();
}
