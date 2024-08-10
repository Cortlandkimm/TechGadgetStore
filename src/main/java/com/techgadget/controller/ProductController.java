package com.techgadget.controller;

import com.techgadget.model.Product;
import com.techgadget.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        try {
            return productService.getAllProducts();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve products", e);
        }
    }

    @GetMapping("/{productSku}")
    public Product getProductBySku(@PathVariable String productSku) {
        try {
            return productService.getProductBySku(productSku);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve product", e);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product newProduct) {
        try {
            return productService.createProduct(newProduct);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create product", e);
        }
    }
}
