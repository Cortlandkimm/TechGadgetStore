package com.techgadget.services;

import com.techgadget.dao.CartDao;
import com.techgadget.dao.UserDao;
import com.techgadget.model.Cart;
import com.techgadget.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartDao cartDao;
    private final UserDao userDao;

    @Value("${tax.api.url}")
    private String taxApiUrl;

    public CartService(CartDao cartDao, UserDao userDao) {
        this.cartDao = cartDao;
        this.userDao = userDao;
    }

    public List<Cart> getCartByUserId(Long userId) {
        return cartDao.getCartByUserId(userId);
    }

    public Cart addItemToCart(Long userId, String productSku, int quantity) {
        return cartDao.addItemToCart(userId, productSku, quantity);
    }

    public Cart removeItemFromCart(Long userId, String productSku) {
        return cartDao.removeItemFromCart(userId, productSku);
    }

    public void clearCart(Long userId) {
        cartDao.clearCart(userId);
    }

    public CartSummary getCartSummary(Long userId) {
        List<Cart> cartItems = cartDao.getCartByUserId(userId);
        BigDecimal subtotal = calculateSubtotal(cartItems);
        User user = userDao.getUserById(userId.intValue());

        BigDecimal taxRate = getTaxRate(user.getState());
        BigDecimal taxAmount = subtotal.multiply(taxRate);
        BigDecimal total = subtotal.add(taxAmount);

        return new CartSummary(cartItems, subtotal, taxAmount, total);
    }

    private BigDecimal calculateSubtotal(List<Cart> cartItems) {
        return cartItems.stream()
                .map(cart -> BigDecimal.valueOf(cart.getPrice()).multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getTaxRate(String state) {
        // Implement tax API call here
        return BigDecimal.ZERO; // Placeholder implementation
    }

    public static class CartSummary {
        private final List<Cart> items;
        private final BigDecimal subtotal;
        private final BigDecimal tax;
        private final BigDecimal total;

        public CartSummary(List<Cart> items, BigDecimal subtotal, BigDecimal tax, BigDecimal total) {
            this.items = items;
            this.subtotal = subtotal;
            this.tax = tax;
            this.total = total;
        }

        public List<Cart> getItems() {
            return items;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }
}
