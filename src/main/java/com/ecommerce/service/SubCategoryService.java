package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.SubCategory;
import com.ecommerce.repository.SubCategoryRepository;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    // ==========================
    // GET ALL SUB CATEGORIES
    // ==========================

    public List<SubCategory> getAllSubCategories() {

        return subCategoryRepository.findAll();

    }

    // ==========================
    // GET SUB CATEGORY
    // ==========================

    public SubCategory getSubCategory(Long id) {

        return subCategoryRepository.findById(id).orElse(null);

    }

    // ==========================
    // SAVE
    // ==========================

    public SubCategory save(SubCategory subCategory) {

        return subCategoryRepository.save(subCategory);

    }

    // ==========================
    // UPDATE
    // ==========================

    public SubCategory update(Long id, SubCategory subCategory) {

        SubCategory existing =
                subCategoryRepository.findById(id).orElse(null);

        if (existing == null) {

            return null;

        }

        existing.setName(subCategory.getName());

        existing.setImage(subCategory.getImage());

        existing.setActive(subCategory.getActive());

        existing.setCategory(subCategory.getCategory());

        return subCategoryRepository.save(existing);

    }

    // ==========================
    // DELETE
    // ==========================

    public void delete(Long id) {

        subCategoryRepository.deleteById(id);

    }

    // ==========================
    // SEARCH
    // ==========================

    public List<SubCategory> search(String keyword) {

        return subCategoryRepository
                .findByNameContainingIgnoreCase(keyword);

    }

    // ==========================
    // GET BY CATEGORY
    // ==========================

    public List<SubCategory> getByCategory(Category category) {

        return subCategoryRepository.findByCategory(category);

    }

    // ==========================
    // ACTIVE SUB CATEGORIES
    // ==========================

    public List<SubCategory> getActiveSubCategories() {

        return subCategoryRepository.findByActiveTrue();

    }

    // ==========================
    // ACTIVE BY CATEGORY
    // ==========================

    public List<SubCategory> getActiveByCategory(Category category) {

        return subCategoryRepository
                .findByCategoryAndActiveTrue(category);

    }

    // ==========================
    // COUNT
    // ==========================

    public long countSubCategories() {

        return subCategoryRepository.count();

    }

    // ==========================
    // DUPLICATE CHECK
    // ==========================

    public boolean exists(String name) {

        return subCategoryRepository
                .existsByNameIgnoreCase(name);

    }

}