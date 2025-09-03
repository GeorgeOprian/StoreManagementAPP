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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        Product product = findProductById(id);
        return mapper.toDto(product);
    }

    public ProductDto getProductByBarcode(String barcode) {
        Product product = repository.findByBarcode(barcode).orElseThrow(() -> new NotFoundException("Product not found by barcode", 4042));
        return mapper.toDto(product);
    }

    public ProductDto createProduct(ProductDto dto) {
        Optional<Product> productOptional = repository.findByBarcode(dto.getBarcode());
        if (productOptional.isPresent()) {
            throw new BadRequestException("Another product with the same barcode already exists", 4001);
        }
        var entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public ProductDto updateProduct(UUID id, ProductDto dto) {

        Product product = findProductById(id);

        if (!dto.getBarcode().equals(product.getBarcode()) && repository.findByBarcode(dto.getBarcode()).isPresent()) {
            throw new BadRequestException("Another product with the same barcode already exists", 4001);
        }

        mapper.updateEntityFromDto(dto, product);

        log.info("Updating product id={} with dto id={}", product.getId(), dto.getId());

        return mapper.toDto(repository.save(product));

    }

    public boolean deleteProduct(UUID id) {
        if (!repository.existsById(id))
            return false;
        repository.deleteById(id);
        return true;
    }

    @Override
    public ProductDto updatePrice(UUID id, BigDecimal newPrice) {
        Product product = findProductById(id);

        product.setPrice(newPrice);
        return mapper.toDto(repository.save(product));
    }

    private Product findProductById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Product not found", 4041));
    }
}

