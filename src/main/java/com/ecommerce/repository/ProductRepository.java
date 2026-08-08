package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // ==========================
    // GLOBAL SEARCH
    // ==========================

    @Query("""
            SELECT p
            FROM Product p
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.subCategory.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<Product> searchProducts(
            @Param("keyword") String keyword);

    // ==========================
    // BEST SELLERS
    // ==========================

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.bestSeller = true
            ORDER BY p.id DESC
            """)
    List<Product> findBestSellerProducts();

    // ==========================
    // NEW ARRIVALS
    // ==========================

    List<Product> findTop4ByOrderByIdDesc();

    // ==========================
    // ALL BRANDS
    // ==========================

    @Query("""
            SELECT DISTINCT p.brand
            FROM Product p
            WHERE p.brand IS NOT NULL
            ORDER BY p.brand
            """)
    List<String> findDistinctBrands();

    // ==========================
    // HOME PAGE BRANDS
    // ==========================

    @Query("""
            SELECT DISTINCT p.brand
            FROM Product p
            WHERE p.brand IN (
                'WROGN',
                'SNITCH',
                'Allen Solly',
                'US Polo',
                'PUMA',
                'NIKE'
            )
            ORDER BY p.brand
            """)
    List<String> findHomeBrands();

    // ==========================
    // PRODUCTS BY BRAND
    // ==========================

    List<Product> findByBrandIgnoreCase(String brand);

    // ==========================
    // PRODUCTS BY SUB CATEGORY
    // ==========================

    List<Product> findBySubCategoryNameIgnoreCase(String name);

    // ==========================
    // PRODUCTS BY CATEGORY
    // ==========================

    List<Product> findByCategoryId(Long categoryId);

    // ==========================
    // PRODUCTS BY SUB CATEGORY ID
    // ==========================

    List<Product> findBySubCategoryId(Long subCategoryId);

    // ==========================
    // SEARCH BY NAME
    // ==========================

    List<Product> findByNameContainingIgnoreCase(String keyword);

    // ==========================
    // LOW STOCK
    // ==========================

    List<Product> findTop5ByQuantityLessThanEqualOrderByQuantityAsc(Integer quantity);

}