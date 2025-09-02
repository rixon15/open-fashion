package com.openfashion.openfasion_marketplace.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    //Notes: We are using nested DTOs for building the Response object to fix the following concerns:
    //Serialization issues -> Lazy-loaded fields
    //Security Risks -> Might expose data from the Entity we don't want to
    //Tight Coupling -> The Apis response is tightly coupled to the database schema

    private Long id;
    private BigDecimal price;
    private String brand;
    private String description;
    private String name;
    private List<ProductVariantResponseDto> variants;

}
