package com.zen.ala.domain.port.in;

import com.zen.ala.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Product inbound port
 *
 * <p>Used to manage products from the application
 */
public interface ProductInboundPort {

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
