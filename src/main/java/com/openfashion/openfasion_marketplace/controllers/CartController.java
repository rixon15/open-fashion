package com.openfashion.openfasion_marketplace.controllers;


import com.openfashion.openfasion_marketplace.models.dtos.request.CartRequestDto;
import com.openfashion.openfasion_marketplace.services.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addItem(@RequestBody CartRequestDto cartRequestDto) {
        return cartService.addItem(cartRequestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Long id) {
        return cartService.deleteItem(id);
    }

    @PutMapping("/")
    public String updateItem(@RequestBody CartRequestDto cartRequestDto) {
        return cartService.updateItem(cartRequestDto.getId(), cartRequestDto.getQuantity());
    }

}
