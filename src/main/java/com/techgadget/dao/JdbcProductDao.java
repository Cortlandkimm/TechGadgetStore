package com.techgadget.dao;

import com.techgadget.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcProductDao implements ProductDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            products.add(mapRowToProduct(results));
        }
        return products;
    }

    @Override
    public Product getProductBySku(String productSku) {
        String sql = "SELECT * FROM products WHERE product_sku = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, productSku);
        if (results.next()) {
            return mapRowToProduct(results);
        }
        return null;
    }

    @Override
    public Product createProduct(Product newProduct) {
        String insertSql = "INSERT INTO products (product_sku, name, description, price, image_name) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING product_sku";
        String productSku = jdbcTemplate.queryForObject(insertSql, String.class,
                newProduct.getProductSku(), newProduct.getName(), newProduct.getDescription(),
                newProduct.getPrice(), newProduct.getImageName());

        return getProductBySku(productSku);
    }

    private Product mapRowToProduct(SqlRowSet rs) {
        Product product = new Product();
        product.setProductSku(rs.getString("product_sku"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getDouble("price"));
        product.setImageName(rs.getString("image_name"));
        return product;
    }
}

