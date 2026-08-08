package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.SubCategory;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.SubCategoryService;

@RestController
@RequestMapping("/api/subcategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private CategoryService categoryService;

    // ==========================
    // GET SUB CATEGORIES BY CATEGORY
    // ==========================

    @GetMapping("/category/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategory(
            @PathVariable Long categoryId) {

        Category category = categoryService.getCategory(categoryId);

        if (category == null) {
            return List.of();
        }

        return subCategoryService.getByCategory(category);
    }

    // ==========================
    // GET ALL SUB CATEGORIES
    // ==========================

    @GetMapping
    public List<SubCategory> getAllSubCategories() {

        return subCategoryService.getAllSubCategories();

    }

}