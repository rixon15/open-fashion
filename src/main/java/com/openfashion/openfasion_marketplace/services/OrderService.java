package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.dtos.request.OrderItemDto;
import com.openfashion.openfasion_marketplace.models.dtos.request.OrderRequestDto;
import com.openfashion.openfasion_marketplace.models.dtos.request.ShippingDetailsDto;
import com.openfashion.openfasion_marketplace.models.entities.*;
import com.openfashion.openfasion_marketplace.models.mappers.OrderMapper;
import com.openfashion.openfasion_marketplace.repositories.OrderRepository;
import com.openfashion.openfasion_marketplace.repositories.ProductVariantRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class OrderService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final OrderMapper orderMapper;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, AuthenticationService authenticationService, OrderMapper orderMapper, ProductVariantRepository productVariantRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.orderMapper = orderMapper;
        this.productVariantRepository = productVariantRepository;
        this.orderRepository = orderRepository;
    }

    public String createOrder(OrderRequestDto orderRequestDto) {

        User currentUser = userRepository.findByUsername(authenticationService.currentUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderMapper.toOrder(orderRequestDto);
        ShippingDetail shippingDetails = orderMapper.toShippingDetail(orderRequestDto.getShippingDetails());

        order.setUser(currentUser);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingDetails(shippingDetails);

        shippingDetails.setOrder(order);

        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(OrderItemDto itemDto : orderRequestDto.getItems()) {
           ProductVariant productVariant = productVariantRepository.findById(itemDto.getProductVariantId())
                   .orElseThrow(() -> new RuntimeException("Product variant not found"));

           OrderItem orderItem = orderMapper.toOrderItem(itemDto);

           orderItem.setProductVariant(productVariant);
           orderItem.setPriceAtPurchase(productVariant.getCatalogItem().getPrice());
           orderItem.setOrder(order);

           orderItems.add(orderItem);
           totalPrice = totalPrice.add(orderItem.getPriceAtPurchase().multiply(new BigDecimal(orderItem.getQuantity())));

        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        return "Order with ID: " + order.getId() + " created successfully";
    }

    public String deleteOrder(Long id) {

        Optional<Order> order = orderRepository.findById(id);

        if(order.isPresent()) {
            orderRepository.deleteById(id);
        } else {
            throw new RuntimeException("Order not found");
        }

        return "Order with ID: " + order.get().getId() + " deleted successfully";
    }

    @Transactional
    public String updateOrder(Long id, OrderRequestDto orderRequestDto) {

        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean isModified = false;

        if (orderRequestDto.getStatus() != null && !orderRequestDto.getStatus().isBlank()) {
            OrderStatus newStatus = OrderStatus.valueOf(orderRequestDto.getStatus());
            if (!existingOrder.getStatus().equals(newStatus)) {
                existingOrder.setStatus(newStatus);
                isModified = true;
            }
        }


        if (orderRequestDto.getShippingDetails() != null) {
            ShippingDetail existingShippingDetail = existingOrder.getShippingDetails();
            ShippingDetailsDto shippingDto = orderRequestDto.getShippingDetails();

            existingShippingDetail.setAddressLine1(shippingDto.getAddressLine1());
            existingShippingDetail.setAddressLine2(shippingDto.getAddressLine2());
            existingShippingDetail.setCity(shippingDto.getCity());
            existingShippingDetail.setStateProvince(shippingDto.getStateProvince());
            existingShippingDetail.setPostalCode(shippingDto.getPostalCode());
            existingShippingDetail.setCountry(shippingDto.getCountry());
            isModified = true;
        }


        if (orderRequestDto.getItems() != null) {
            // Clear existing items, letting JPA's orphanRemoval handle the deletion
            existingOrder.getItems().clear();

            BigDecimal newTotalPrice = BigDecimal.ZERO;

            for (OrderItemDto itemDto : orderRequestDto.getItems()) {
                ProductVariant productVariant = productVariantRepository.findById(itemDto.getProductVariantId())
                        .orElseThrow(() -> new RuntimeException("Product variant not found: " + itemDto.getProductVariantId()));

                OrderItem orderItem = new OrderItem();
                orderItem.setProductVariant(productVariant);
                orderItem.setQuantity(itemDto.getQuantity());

                orderItem.setPriceAtPurchase(productVariant.getCatalogItem().getPrice());
                orderItem.setOrder(existingOrder);

                existingOrder.getItems().add(orderItem);
                newTotalPrice = newTotalPrice.add(orderItem.getPriceAtPurchase().multiply(new BigDecimal(orderItem.getQuantity())));
            }

            existingOrder.setTotalPrice(newTotalPrice);
            isModified = true;
        }

        if (isModified) {
            orderRepository.save(existingOrder);
            return "Order with ID: " + existingOrder.getId() + " updated successfully";
        } else {
            return "No changes to update for order with ID: " + existingOrder.getId();
        }

    }

    public String addItem(Long orderId, OrderItemDto orderItemDto) {

        Order currentOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ProductVariant productVariant = productVariantRepository.findById(orderItemDto.getProductVariantId())
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        Optional<OrderItem> existingItem = currentOrder.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(orderItemDto.getProductVariantId()))
                .findFirst();

        if (existingItem.isPresent()) {
            return "Item with ID: " + existingItem.get().getId() + " already exists in the order";
        }

        OrderItem orderItem = orderMapper.toOrderItem(orderItemDto);
        orderItem.setProductVariant(productVariant);
        orderItem.setOrder(currentOrder);
        orderItem.setPriceAtPurchase(productVariant.getCatalogItem().getPrice());


        currentOrder.getItems().add(orderItem);
        BigDecimal itemTotal = orderItem.getPriceAtPurchase().multiply(new BigDecimal(orderItem.getQuantity()));
        currentOrder.setTotalPrice(currentOrder.getTotalPrice().add(itemTotal));

        orderRepository.save(currentOrder);

        return "Item added successfully to the order with ID: " + currentOrder.getId();

    }

    @Transactional
    public String deleteItem(Long orderId, Long itemId) {

        Order currentOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        Optional<OrderItem> itemToDeleteOptional = currentOrder.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        if (itemToDeleteOptional.isEmpty()) {
            throw new RuntimeException("Item with ID " + itemId + " not found in order.");
        }

        OrderItem itemToDelete = itemToDeleteOptional.get();

        BigDecimal itemTotal = itemToDelete.getPriceAtPurchase().multiply(new BigDecimal(itemToDelete.getQuantity()));
        currentOrder.setTotalPrice(currentOrder.getTotalPrice().subtract(itemTotal));

        currentOrder.getItems().remove(itemToDelete);

        orderRepository.save(currentOrder);

        return "Item with ID: " + itemId + " has been deleted successfully from order " + orderId;
    }


    @Transactional
    public String updateItem(Long orderId, Long itemId, OrderItemDto orderItemDto) {

        Order currentOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        OrderItem existingItem = currentOrder.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item with ID " + itemId + " not found in order."));


        if (orderItemDto.getQuantity() != null && orderItemDto.getQuantity() > 0) {
            existingItem.setQuantity(orderItemDto.getQuantity());
        } else {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        BigDecimal newOrderTotal = currentOrder.getItems().stream()
                .map(item -> item.getPriceAtPurchase().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        currentOrder.setTotalPrice(newOrderTotal);

        orderRepository.save(currentOrder);

        return "Item with ID " + itemId + " updated successfully to the order with ID: " + currentOrder.getId();
    }

    public String updateShippingDetails(Long id, ShippingDetailsDto shippingDetailsDto) {

        Order currentOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ShippingDetail currentShippingDetail = currentOrder.getShippingDetails();

        currentShippingDetail.setCity(shippingDetailsDto.getCity());
        currentShippingDetail.setCountry(shippingDetailsDto.getCountry());
        currentShippingDetail.setAddressLine1(shippingDetailsDto.getAddressLine1());
        currentShippingDetail.setAddressLine2(shippingDetailsDto.getAddressLine2());
        currentShippingDetail.setStateProvince(shippingDetailsDto.getStateProvince());
        currentShippingDetail.setPostalCode(shippingDetailsDto.getPostalCode());

        orderRepository.save(currentOrder);

        return "Shipping details updated successfully to the order with ID: " + currentOrder.getId();
    }
}
