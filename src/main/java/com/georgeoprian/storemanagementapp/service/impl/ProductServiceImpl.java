package com.georgeoprian.storemanagementapp.service.impl;

import com.georgeoprian.storemanagementapp.dtos.ProductDto;
import com.georgeoprian.storemanagementapp.exception.BadRequestException;
import com.georgeoprian.storemanagementapp.exception.NotFoundException;
import com.georgeoprian.storemanagementapp.mappers.ProductMapper;
import com.georgeoprian.storemanagementapp.model.Product;
import com.georgeoprian.storemanagementapp.repository.ProductRepository;
import com.georgeoprian.storemanagementapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public List<ProductDto> getAllProducts() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(UUID id) {
        Product product = repository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        return mapper.toDto(product);
    }

    public ProductDto getProductByBarcode(String barcode) {
        Product product = repository.findByBarcode(barcode).orElseThrow(() -> new NotFoundException("Product not found by barcode"));
        return mapper.toDto(product);
    }

    public ProductDto createProduct(ProductDto dto) {
        checkIfBarcodeExists(dto);
        var entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    private void checkIfBarcodeExists(ProductDto dto) {
        if (repository.existsByBarcode(dto.getBarcode())) {
            throw new NotFoundException("Product with barcode already exists");
        }
    }

    public ProductDto updateProduct(UUID id, ProductDto dto) {
        try {
            checkIfBarcodeExists(dto);

            Product product = repository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
            mapper.updateEntityFromDto(dto, product);

            log.info("Updating product id={} with dto id={}", product.getId(), dto.getId());

            return mapper.toDto(repository.save(product));
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    public boolean deleteProduct(UUID id) {
        if (!repository.existsById(id))
            return false;
        repository.deleteById(id);
        return true;
    }
}

