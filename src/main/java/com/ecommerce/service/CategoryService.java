package com.ecommerce.service;

import com.ecommerce.entity.Category;
import com.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public List<Category> getAllCategories() {

        return repository.findAll();

    }

    public Category saveCategory(Category category) {

        return repository.save(category);

    }

    public void deleteCategory(Long id) {

        repository.deleteById(id);

    }

}