package com.georgeoprian.storemanagementapp.mappers;

import com.georgeoprian.storemanagementapp.dtos.ProductDto;
import com.georgeoprian.storemanagementapp.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product entity);

    Product toEntity(ProductDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProductDto dto, @MappingTarget Product entity);
}

