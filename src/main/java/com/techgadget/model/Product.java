package com.techgadget.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public class Product {

    @NotBlank(message = "Product SKU is required")
    private String productSku;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @DecimalMin(value = "0.0", message = "Price should be greater than or equal to 0")
    private double price;

    @NotBlank(message = "Image name is required")
    private String imageName;

    // Getters and Setters

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}

