package com.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private String description;

    private Double price;

    private Integer quantity;

    private String image;

    // ==========================
    // BEST SELLER
    // ==========================

    private Boolean bestSeller = false;

    // ==========================
    // CATEGORY
    // ==========================

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // ==========================
    // SUB CATEGORY
    // ==========================

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    // ==========================
    // CONSTRUCTORS
    // ==========================

    public Product() {
    }

    public Product(Long id,
                   String name,
                   String brand,
                   String description,
                   Double price,
                   Integer quantity,
                   String image,
                   Boolean bestSeller,
                   Category category,
                   SubCategory subCategory) {

        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.bestSeller = bestSeller;
        this.category = category;
        this.subCategory = subCategory;
    }

    // ==========================
    // GETTERS & SETTERS
    // ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(Boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

}