package com.techgadget.dao;

import com.techgadget.model.Cart;
import java.util.List;

public interface CartDao {
    List<Cart> getCartByUserId(Long userId);
    Cart getCartByUserIdAndProductSku(Long userId, String productSku);
    Cart addItemToCart(Long userId, String productSku, int quantity);
    Cart removeItemFromCart(Long userId, String productSku);
    void clearCart(Long userId);
}
