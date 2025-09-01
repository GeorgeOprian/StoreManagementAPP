package com.georgeoprian.storemanagementapp.repository;

import com.georgeoprian.storemanagementapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

}
