package com.techgadget.services;

import com.techgadget.dao.ProductDao;
import com.techgadget.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public Product getProductBySku(String productSku) {
        return productDao.getProductBySku(productSku);
    }

    public Product createProduct(Product newProduct) {
        return productDao.createProduct(newProduct);
    }
}
