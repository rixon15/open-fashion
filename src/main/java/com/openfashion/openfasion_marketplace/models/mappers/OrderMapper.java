package com.openfashion.openfasion_marketplace.models.mappers;

import com.openfashion.openfasion_marketplace.models.dtos.request.OrderItemDto;
import com.openfashion.openfasion_marketplace.models.dtos.request.OrderRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.request.ShippingDetailsDto;
import com.openfashion.openfasion_marketplace.models.entities.Order;
import com.openfashion.openfasion_marketplace.models.entities.OrderItem;
import com.openfashion.openfasion_marketplace.models.entities.ShippingDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toOrder(OrderRequestDto orderRequestDto);

    ShippingDetail toShippingDetail(ShippingDetailsDto shippingDetailsDto);

    //The mapper should call the toOrderItem method for each element in the list
    OrderItem toOrderItem(OrderItemDto orderItemDto);

}
