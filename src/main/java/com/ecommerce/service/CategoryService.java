package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Category;
import com.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    // ==========================
    // GET ALL CATEGORIES
    // ==========================

    public List<Category> getAllCategories() {

        return repository.findAll();

    }

    // ==========================
    // GET CATEGORY BY ID
    // ==========================

    public Category getCategory(Long id) {

        return repository.findById(id).orElse(null);

    }

    // ==========================
    // SAVE CATEGORY
    // ==========================

    public Category saveCategory(Category category) {

        return repository.save(category);

    }

    // ==========================
    // UPDATE CATEGORY
    // ==========================

    public Category updateCategory(Long id, Category category) {

        Category existing = repository.findById(id).orElse(null);

        if (existing == null) {

            return null;

        }

        existing.setName(category.getName());

        return repository.save(existing);

    }

    // ==========================
    // DELETE CATEGORY
    // ==========================

    public void deleteCategory(Long id) {

        repository.deleteById(id);

    }

    // ==========================
    // SEARCH CATEGORY
    // ==========================

    public List<Category> search(String keyword) {

        return repository.findByNameContainingIgnoreCase(keyword);

    }

    // ==========================
    // COUNT CATEGORIES
    // ==========================

    public long countCategories() {

        return repository.count();

    }

}