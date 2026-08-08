package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    // Get all subcategories of a category
    List<SubCategory> findByCategory(Category category);

    // Active subcategories
    List<SubCategory> findByActiveTrue();

    // Active subcategories by category
    List<SubCategory> findByCategoryAndActiveTrue(Category category);

    // Search
    List<SubCategory> findByNameContainingIgnoreCase(String keyword);

    // Duplicate check
    boolean existsByNameIgnoreCase(String name);

}