package com.zen.ala.application.service;

import com.zen.ala.domain.model.Product;
import com.zen.ala.domain.port.in.ProductRetriever;
import com.zen.ala.domain.port.out.ProductLoader;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductRetrieverImpl implements ProductRetriever {

  private final ProductLoader productLoader;

  @Override
  public Product getProductById(UUID id) {
    return productLoader.findProductById(id);
  }

  @Override
  public List<Product> getAllProducts() {
    return productLoader.findAllProducts();
  }
}
