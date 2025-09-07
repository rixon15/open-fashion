package com.openfashion.openfasion_marketplace.models.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private Long productVariantId;
    private Integer quantity;

}
