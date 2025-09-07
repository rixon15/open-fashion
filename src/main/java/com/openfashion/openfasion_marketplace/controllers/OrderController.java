package com.openfashion.openfasion_marketplace.controllers;

import com.openfashion.openfasion_marketplace.models.dtos.request.OrderItemDto;
import com.openfashion.openfasion_marketplace.models.dtos.request.OrderRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.request.ShippingDetailsDto;
import com.openfashion.openfasion_marketplace.services.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create order -> big order dto to create shipping details and order_item for each item the user has in the cart
    // Update order -> Ability to update an order by adding,removing order_items from the list or
    // changing their quantity. This is an admin feature
    // Update shipping detail -> Update the shipping detail admin feature
    // Delete order -> delete the entire order, admin feature
    // Update order

    @PostMapping("/add")
    public String createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }

    @PutMapping("/{id}")
    public String updateOrder(@PathVariable Long id, @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.updateOrder(id, orderRequestDto);
    }

    @PostMapping("/{orderId}/addItem")
    public String addItem(@PathVariable Long orderId, @RequestBody OrderItemDto orderItemDto) {
        return orderService.addItem(orderId, orderItemDto);
    }

    @DeleteMapping("/{orderId}/item/{itemId}")
    public String deleteItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.deleteItem(orderId, itemId);
    }

    //Quantity only.
    @PutMapping("/{orderId}/item/{itemId}")
    public String updateItem(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody OrderItemDto orderItemDto) {
        return orderService.updateItem(orderId,itemId,orderItemDto);
    }

    @PutMapping("/{id}/shipping")
    public String updateShipping(@PathVariable Long id, @RequestBody ShippingDetailsDto shippingDetailsDto) {
        return orderService.updateShippingDetails(id, shippingDetailsDto);
    }


}
