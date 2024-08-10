package com.techgadget.controller;

import com.techgadget.model.Cart;
import com.techgadget.model.CartDto;
import com.techgadget.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> getUserCart(@RequestParam Long userId) {
        try {
            return cartService.getCartByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve cart", e);
        }
    }

    @GetMapping("/summary")
    public CartService.CartSummary getCartSummary(@RequestParam Long userId) {
        try {
            return cartService.getCartSummary(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve cart summary", e);
        }
    }

    @PostMapping
    public Cart addItemToCart(@RequestParam Long userId, @RequestBody CartDto cartDto) {
        try {
            return cartService.addItemToCart(userId, cartDto.getProductSku(), cartDto.getQuantity());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to add item to cart", e);
        }
    }

    @DeleteMapping("/{productSku}")
    public Cart removeItemFromCart(@RequestParam Long userId, @PathVariable String productSku) {
        try {
            return cartService.removeItemFromCart(userId, productSku);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found in cart", e);
        }
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestParam Long userId) {
        try {
            cartService.clearCart(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to clear cart", e);
        }
    }
}

