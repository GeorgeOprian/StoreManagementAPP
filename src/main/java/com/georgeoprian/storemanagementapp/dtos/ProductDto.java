package com.georgeoprian.storemanagementapp.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private UUID id;

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotBlank
    @Size(min = 14, max = 14)
    private String barcode;

    @Size(max = 2000)
    private String description;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;

    @NotBlank
    @Size(max = 100)
    private String department;

    private String status; // ACTIVE, INACTIVE, etc.
}

