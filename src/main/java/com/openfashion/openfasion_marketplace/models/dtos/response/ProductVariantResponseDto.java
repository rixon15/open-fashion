package com.openfashion.openfasion_marketplace.models.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponseDto {

    private Long id;
    private String color;
    private String size;

}
