package com.zen.ala.domain.port.out;

import com.zen.ala.domain.model.Product;
import java.util.List;
import java.util.UUID;

/**
 * Product loader port
 *
 * <p>Used to load products from the infrastructure
 */
public interface ProductOutboundPort {

  /**
   * Find product by id
   *
   * @param id - product id
   * @return Product
   */
  Product findProductById(UUID id);

  /**
   * Find all products
   *
   * @return List<Product>
   */
  List<Product> findAllProducts();

  /**
   * Save product
   *
   * @param product - product to save
   * @return Product
   */
  Product saveProduct(Product product);

  /**
   * Update product
   *
   * @param product - product to update
   * @return Product
   */
  Product updateProduct(Product product);

  /**
   * Delete product
   *
   * @param id - product id
   */
  void deleteProduct(UUID id);
}
