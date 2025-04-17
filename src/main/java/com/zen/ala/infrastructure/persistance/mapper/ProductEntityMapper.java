package com.zen.ala.infrastructure.persistance.mapper;

import com.zen.ala.domain.model.Product;
import com.zen.ala.infrastructure.persistance.entity.ProductEntity;
import org.mapstruct.Mapper;

/** ProductEntityMapper is responsible for mapping between Product and ProductEntity. */
@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

  Product toDomain(ProductEntity entity);

  ProductEntity toEntity(Product product);
}
