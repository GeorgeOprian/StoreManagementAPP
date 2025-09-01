package com.georgeoprian.storemanagementapp.service;

import com.georgeoprian.storemanagementapp.dtos.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDto> getAllProducts();
    ProductDto getProductById(UUID id);
    ProductDto getProductByBarcode(String barcode);
    ProductDto createProduct(ProductDto dto);
    ProductDto updateProduct(UUID id, ProductDto dto);
    boolean deleteProduct(UUID id);
}
