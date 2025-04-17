package com.zen.ala.infrastructure.web.mapper;

import com.zen.ala.domain.model.Product;
import com.zen.ala.infrastructure.web.dto.ProductResponseDto;
import org.mapstruct.Mapper;

/**
 * ProductResponseDtoMapper is an interface that defines methods for mapping between
 * ProductResponseDto and Product domain model.
 *
 * <p>This interface uses MapStruct to generate the implementation at compile time.
 *
 * @see Product
 * @see ProductResponseDto
 */
@Mapper(componentModel = "spring")
public interface ProductResponseDtoMapper {

  ProductResponseDto toDto(Product product);

  Product toDomain(ProductResponseDto dto);
}
