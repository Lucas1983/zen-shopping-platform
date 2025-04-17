package com.zen.ala.infrastructure.web.mapper;

import com.zen.ala.domain.model.Product;
import com.zen.ala.infrastructure.web.dto.ProductRequestDto;
import java.util.UUID;
import org.mapstruct.Mapper;

/**
 * ProductRequestDtoMapper is an interface that defines methods for mapping between
 * ProductRequestDto and Product domain model.
 *
 * <p>This interface uses MapStruct to generate the implementation at compile time.
 *
 * @see Product
 * @see ProductRequestDto
 */
@Mapper(componentModel = "spring")
public interface ProductRequestDtoMapper {

  Product toDomain(ProductRequestDto dto);

  Product toDomain(UUID id, ProductRequestDto dto);
}
