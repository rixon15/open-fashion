package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.dtos.request.CartRequestDto;
import com.openfashion.openfasion_marketplace.models.entities.Cart;
import com.openfashion.openfasion_marketplace.models.entities.CartItem;
import com.openfashion.openfasion_marketplace.models.entities.ProductVariant;
import com.openfashion.openfasion_marketplace.models.entities.User;
import com.openfashion.openfasion_marketplace.repositories.CartRepository;
import com.openfashion.openfasion_marketplace.repositories.ProductVariantRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
public class CartService {


    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductVariantRepository productVariantRepository;

    public CartService(AuthenticationService authenticationService, UserRepository userRepository, CartRepository cartRepository, ProductVariantRepository productVariantRepository) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Transactional
    public String addItem(CartRequestDto cartRequestDto) {

        User currentUser = userRepository.findByUsername(authenticationService.currentUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProductVariant product = productVariantRepository.findById(cartRequestDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));



        //Check if current user has a cart
        Optional<Cart> currentCartOptional = cartRepository.findByUserId(currentUser.getId());

        Cart cart;

        if (currentCartOptional.isPresent()) {
            cart = currentCartOptional.get();
        } else {
            cart = new Cart();
            cart.setUser(currentUser);
            cart.setCartItems(new HashSet<>());
        }

        CartItem cartItem = new CartItem();
        cartItem.setProductVariant(product);
        cartItem.setQuantity(cartRequestDto.getQuantity());
        cartItem.setCart(cart);

        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);

        return "Product Successfully added to Cart";
    }


    public String deleteItem(Long id) {

        User currentUser = userRepository.findByUsername(authenticationService.currentUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> currentCartOptional = cartRepository.findByUserId(currentUser.getId());

        if (currentCartOptional.isPresent()) {
            Cart currentCart = currentCartOptional.get();
            boolean removeFlag = currentCart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(id));
            if(removeFlag) {
                cartRepository.save(currentCart);
                return "Product Successfully deleted from Cart";
            } else {
                return "Product not found";
            }
        } else {
            return "Cart is empty";
        }
    }

    public String updateItem(Long id, int quantity) {
        User currentUser = userRepository.findByUsername(authenticationService.currentUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> currentCartOptional = cartRepository.findByUserId(currentUser.getId());

        if(currentCartOptional.isPresent()) {
            Cart currentCart = currentCartOptional.get();
            CartItem cartItem = currentCart.getCartItems().stream().filter(
                    item -> item.getId().equals(id)).findFirst().orElse(null);

            if(cartItem != null && quantity >= 1) {
                cartItem.setQuantity(quantity);
                return "Product Successfully updated";
            } else if(quantity <= 0) {
                throw new RuntimeException("Quantity should be greater than zero");
            }
        }

        return "Cart is empty";
    }
}
