package com.openfashion.openfasion_marketplace.models.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    private String status;
    private BigDecimal totalPrice;

    private Set<OrderItemDto> items;
    private ShippingDetailsDto shippingDetails;


}
