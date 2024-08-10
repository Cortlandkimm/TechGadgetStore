package com.techgadget.dao;

import com.techgadget.model.Cart;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCartDao implements CartDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCartDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Cart> getCartByUserId(Long userId) {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT s.id, s.user_id, s.product_sku, s.quantity, p.price " +
                "FROM shopping_cart s JOIN products p ON p.product_sku = s.product_sku " +
                "WHERE s.user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            cartItems.add(mapRowToCart(results));
        }
        return cartItems;
    }

    @Override
    public Cart getCartByUserIdAndProductSku(Long userId, String productSku) {
        String sql = "SELECT s.id, s.user_id, s.product_sku, s.quantity, p.price " +
                "FROM shopping_cart s JOIN products p ON p.product_sku = s.product_sku " +
                "WHERE s.user_id = ? AND s.product_sku = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, productSku);
        if (results.next()) {
            return mapRowToCart(results);
        }
        return null;
    }

    @Override
    public Cart addItemToCart(Long userId, String productSku, int quantity) {
        Cart existingItem = getCartByUserIdAndProductSku(userId, productSku);
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            String updateSql = "UPDATE shopping_cart SET quantity = ? WHERE id = ? RETURNING *";
            SqlRowSet results = jdbcTemplate.queryForRowSet(updateSql, newQuantity, existingItem.getId());
            if (results.next()) {
                return mapRowToCart(results);
            }
        } else {
            String insertSql = "INSERT INTO shopping_cart (user_id, product_sku, quantity) " +
                    "VALUES (?, ?, ?) RETURNING *";
            SqlRowSet results = jdbcTemplate.queryForRowSet(insertSql, userId, productSku, quantity);
            if (results.next()) {
                return mapRowToCart(results);
            }
        }
        return null;
    }

    @Override
    public Cart removeItemFromCart(Long userId, String productSku) {
        String selectSql = "SELECT * FROM shopping_cart WHERE user_id = ? AND product_sku = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(selectSql, userId, productSku);
        if (results.next()) {
            Cart cart = mapRowToCart(results);
            String deleteSql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_sku = ? RETURNING *";
            results = jdbcTemplate.queryForRowSet(deleteSql, userId, productSku);
            if (results.next()) {
                return cart;
            }
        }
        return null;
    }

    @Override
    public void clearCart(Long userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    private Cart mapRowToCart(SqlRowSet rs) {
        Cart cart = new Cart();
        cart.setId(rs.getLong("id"));
        cart.setUserId(rs.getLong("user_id"));
        cart.setProductSku(rs.getString("product_sku"));
        cart.setQuantity(rs.getInt("quantity"));
        cart.setPrice(rs.getDouble("price"));
        return cart;
    }
}
