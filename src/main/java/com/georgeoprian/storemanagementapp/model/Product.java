package com.georgeoprian.storemanagementapp.model;

import com.georgeoprian.storemanagementapp.model.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "product",
        indexes = {
                @Index(name = "idx_product_name", columnList = "name"),
                @Index(name = "idx_product_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_product_barcode", columnNames = "barcode")
        }
)
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank
    @Size(min = 14, max = 14, message = "Barcode must be exactly 14 characters")
    @Column(nullable = false, length = 14, unique = true)
    private String barcode;

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Size(max = 100)
    @Column(length = 100)
    private String department;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

}

