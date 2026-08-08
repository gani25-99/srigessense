package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ==========================
    // GET ALL PRODUCTS
    // ==========================

    public List<Product> getAllProducts() {

        return productRepository.findAll();

    }

    // ==========================
    // GET PRODUCT BY ID
    // ==========================

    public Product getProduct(Long id) {

        return productRepository.findById(id).orElse(null);

    }

    // ==========================
    // SAVE PRODUCT
    // ==========================

    public Product save(Product product) {

        return productRepository.save(product);

    }

    // ==========================
    // UPDATE PRODUCT
    // ==========================

public Product update(Long id, Product product) {

    Product existing = productRepository.findById(id).orElse(null);

    if (existing == null) {
        return null;
    }

    existing.setName(product.getName());
    existing.setDescription(product.getDescription());
    existing.setPrice(product.getPrice());
    existing.setQuantity(product.getQuantity());

    // Keep the old image if no new image is provided
    if (product.getImage() != null && !product.getImage().isBlank()) {
        existing.setImage(product.getImage());
    }

    existing.setCategory(product.getCategory());
    existing.setSubCategory(product.getSubCategory());

    return productRepository.save(existing);
}
    // ==========================
    // DELETE PRODUCT
    // ==========================

    public void delete(Long id) {

        productRepository.deleteById(id);

    }

    // ==========================
    // SEARCH PRODUCTS
    // ==========================

    public List<Product> searchProducts(String keyword) {

        return productRepository.findByNameContainingIgnoreCase(keyword);

    }

    public List<Product> search(String keyword) {

        return searchProducts(keyword);

    }

    // ==========================
    // PRODUCTS BY CATEGORY
    // ==========================

    public List<Product> getProductsByCategory(Long categoryId) {

        return productRepository.findByCategoryId(categoryId);

    }

    // ==========================
    // PRODUCTS BY SUB CATEGORY
    // ==========================

    public List<Product> getProductsBySubCategory(Long subCategoryId) {

        return productRepository.findBySubCategoryId(subCategoryId);

    }

    // ==========================
    // TOTAL PRODUCTS
    // ==========================

    public long countProducts() {

        return productRepository.count();

    }

    // ==========================
    // LOW STOCK PRODUCTS
    // ==========================

    public List<Product> getLowStockProducts() {

        return productRepository
                .findTop5ByQuantityLessThanEqualOrderByQuantityAsc(5);

    }

    // ==========================
    // TOP SELLING PRODUCTS
    // ==========================

    public List<Product> getTopSellingProducts() {

        return productRepository.findAll()
                .stream()
                .limit(5)
                .toList();

    }

}