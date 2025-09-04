package com.georgeoprian.storemanagementapp.service;

import com.georgeoprian.storemanagementapp.dtos.ProductDto;
import com.georgeoprian.storemanagementapp.exception.BadRequestException;
import com.georgeoprian.storemanagementapp.exception.NotFoundException;
import com.georgeoprian.storemanagementapp.mappers.ProductMapper;
import com.georgeoprian.storemanagementapp.model.Product;
import com.georgeoprian.storemanagementapp.repository.ProductRepository;
import com.georgeoprian.storemanagementapp.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductRepository repository;
    private ProductMapper mapper;
    private ProductServiceImpl service;

    private UUID productId;
    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        mapper = mock(ProductMapper.class);
        service = new ProductServiceImpl(repository, mapper);

        productId = UUID.randomUUID();
        product = new Product();
        product.setId(productId);
        product.setBarcode("08017339003567");
        product.setPrice(BigDecimal.valueOf(10));

        productDto = createProductDto(productId, "Test Product", "08017339003567", BigDecimal.valueOf(10));
    }

    private ProductDto createProductDto(UUID id, String name, String barcode, BigDecimal price) {
        ProductDto dto = new ProductDto();
        dto.setId(id);
        dto.setName(name);
        dto.setBarcode(barcode);
        dto.setDescription("Electronics description");
        dto.setPrice(price);
        dto.setDepartment("Electronics");
        dto.setStatus("ACTIVE");
        return dto;
    }

    @Test
    void getAllProducts_returnsPagedDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(productPage);
        when(mapper.toDto(product)).thenReturn(productDto);

        var result = service.getAllProducts(pageable);

        assertThat(result.content()).containsExactly(productDto);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.last()).isTrue();

        verify(repository).findAll(pageable);
    }


    @Test
    void getProductById_found_returnsDto() {
        when(repository.findById(productId)).thenReturn(Optional.of(product));
        when(mapper.toDto(product)).thenReturn(productDto);

        var result = service.getProductById(productId);

        assertThat(result).isEqualTo(productDto);
    }

    @Test
    void getProductById_notFound_throwsException() {
        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProductById(productId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product not found");
    }

    @Test
    void getProductByBarcode_found_returnsDto() {
        when(repository.findByBarcode("08017339003567")).thenReturn(Optional.of(product));
        when(mapper.toDto(product)).thenReturn(productDto);

        var result = service.getProductByBarcode("08017339003567");

        assertThat(result).isEqualTo(productDto);
    }

    @Test
    void getProductByBarcode_notFound_throwsException() {
        when(repository.findByBarcode("XXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProductByBarcode("XXX"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Product not found by barcode");
    }

    @Test
    void createProduct_duplicateBarcode_throwsException() {
        when(repository.findByBarcode("08017339003567")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.createProduct(productDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Another product with the same barcode already exists");
    }

    @Test
    void createProduct_success_savesAndReturnsDto() {
        when(repository.findByBarcode("08017339003567")).thenReturn(Optional.empty());
        when(mapper.toEntity(productDto)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(productDto);

        var result = service.createProduct(productDto);

        assertThat(result).isEqualTo(productDto);
        verify(repository).save(product);
    }

    @Test
    void updateProduct_notFound_throwsException() {
        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateProduct(productId, productDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateProduct_duplicateBarcode_throwsException() {
        Product another = new Product();
        another.setId(UUID.randomUUID());
        another.setBarcode("08017339003574");

        when(repository.findById(productId)).thenReturn(Optional.of(product));
        when(repository.findByBarcode("08017339003574")).thenReturn(Optional.of(another));

        ProductDto dto = createProductDto(productId, "Updated", "08017339003574", BigDecimal.TEN);

        assertThatThrownBy(() -> service.updateProduct(productId, dto))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void updateProduct_success_updatesAndReturnsDto() {
        when(repository.findById(productId)).thenReturn(Optional.of(product));
        when(repository.findByBarcode("08017339003567")).thenReturn(Optional.of(product)); // no conflict
        when(repository.save(product)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(productDto);

        ProductDto dto = createProductDto(productId, "Updated", "08017339003567", BigDecimal.valueOf(20));

        var result = service.updateProduct(productId, dto);

        assertThat(result).isEqualTo(productDto);
        verify(mapper).updateEntityFromDto(dto, product);
        verify(repository).save(product);
    }

    @Test
    void deleteProduct_notFound_returnsFalse() {
        when(repository.existsById(productId)).thenReturn(false);

        boolean result = service.deleteProduct(productId);

        assertThat(result).isFalse();
        verify(repository, never()).deleteById(any());
    }

    @Test
    void deleteProduct_found_deletesAndReturnsTrue() {
        when(repository.existsById(productId)).thenReturn(true);

        boolean result = service.deleteProduct(productId);

        assertThat(result).isTrue();
        verify(repository).deleteById(productId);
    }

    @Test
    void updatePrice_found_updatesPrice() {
        when(repository.findById(productId)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(productDto);

        var result = service.updatePrice(productId, BigDecimal.valueOf(99));

        assertThat(result).isEqualTo(productDto);
        assertThat(product.getPrice()).isEqualByComparingTo("99");
        verify(repository).save(product);
    }

    @Test
    void updatePrice_notFound_throwsException() {
        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updatePrice(productId, BigDecimal.ONE))
                .isInstanceOf(NotFoundException.class);
    }
}
