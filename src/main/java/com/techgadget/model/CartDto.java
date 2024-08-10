package com.techgadget.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CartDto {

    @NotBlank(message = "Product SKU is required")
    private String productSku;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    // Getters and Setters

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
