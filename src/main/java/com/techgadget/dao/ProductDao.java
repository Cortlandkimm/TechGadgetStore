package com.techgadget.dao;

import com.techgadget.model.Product;
import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts();
    Product getProductBySku(String productSku);
    Product createProduct(Product newProduct);
}
