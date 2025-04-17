package com.zen.ala.application.service;

import com.zen.ala.domain.error.InvalidDiscountStrategyException;
import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.port.in.ProductInboundPort;
import com.zen.ala.domain.port.out.ProductOutboundPort;
import com.zen.ala.domain.service.PriceCalculatorService;
import com.zen.ala.domain.service.discount.DiscountStrategy;
import com.zen.ala.domain.service.discount.dict.DiscountPolicy;
import com.zen.ala.domain.service.discount.dict.DiscountType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ProductService is responsible for managing products and calculating discounted prices.
 *
 * <p>This service implements the ProductInboundPort interface and uses the ProductOutboundPort
 * interface to interact with the product repository.
 */
@Service
@RequiredArgsConstructor
public class ProductService implements ProductInboundPort {

  private final ProductOutboundPort productOutboundPort;
  private final PriceCalculatorService priceCalculatorService;

  /**
   * Retrieves a product by its ID.
   *
   * @param id the UUID of the product
   * @return the product
   */
  @Override
  public Product getProductById(UUID id) {
    return productOutboundPort.findProductById(id);
  }

  /**
   * Retrieves all products.
   *
   * @return a list of products
   */
  @Override
  public List<Product> getAllProducts() {
    return productOutboundPort.findAllProducts();
  }

  /**
   * Saves a new product.
   *
   * @param product the product to save
   * @return the saved product
   */
  @Override
  public Product saveProduct(Product product) {
    product.setId(UUID.randomUUID());
    return productOutboundPort.saveProduct(product);
  }

  /**
   * Updates an existing product.
   *
   * @param product the product to update
   * @return the updated product
   */
  @Override
  public Product updateProduct(Product product) {
    return productOutboundPort.updateProduct(product);
  }

  /**
   * Deletes a product by its ID.
   *
   * @param id the UUID of the product
   */
  @Override
  public void deleteProduct(UUID id) {
    productOutboundPort.deleteProduct(id);
  }

  /**
   * Calculates the discounted price based on the unit price, quantity, discount type, and discount
   * policy.
   *
   * @param unitPrice the unit price of the product
   * @param quantity the quantity of the product
   * @param discountType the discount type (e.g., "PERCENTAGE", "FIXED")
   * @param discountPolicy the discount policy (e.g., "BULK", "SEASONAL")
   * @return the discounted price
   */
  @Override
  public BigDecimal calculateDiscountedPrice(
      BigDecimal unitPrice, int quantity, String discountType, String discountPolicy) {

    DiscountType type;
    DiscountPolicy policy;
    try {
      type = DiscountType.valueOf(discountType);
    } catch (IllegalArgumentException e) {
      throw new InvalidDiscountStrategyException("Invalid discount type: " + discountType);
    }
    try {
      policy = DiscountPolicy.valueOf(discountPolicy);
    } catch (IllegalArgumentException e) {
      throw new InvalidDiscountStrategyException("Invalid discount policy: " + discountPolicy);
    }

    DiscountStrategy strategy = new DiscountStrategy(type, policy);

    return priceCalculatorService.calculatePrice(unitPrice, quantity, strategy);
  }
}
