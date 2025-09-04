package com.georgeoprian.storemanagementapp.service;

import com.georgeoprian.storemanagementapp.dtos.PageResponseDto;
import com.georgeoprian.storemanagementapp.dtos.ProductDto;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductService {

    PageResponseDto<ProductDto> getAllProducts(Pageable pageable);
    ProductDto getProductById(UUID id);
    ProductDto getProductByBarcode(String barcode);
    ProductDto createProduct(ProductDto dto);
    ProductDto updateProduct(UUID id, ProductDto dto);
    boolean deleteProduct(UUID id);
    ProductDto updatePrice(UUID id, BigDecimal newPrice);
}
