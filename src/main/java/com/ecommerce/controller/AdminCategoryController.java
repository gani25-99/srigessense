package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.entity.Category;
import com.ecommerce.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    // ==========================
    // LIST CATEGORIES
    // ==========================

    @GetMapping
    public String categories(Model model) {

        model.addAttribute("categories",
                categoryService.getAllCategories());

        model.addAttribute("category",
                new Category());

        return "admin-categories";

    }

    // ==========================
    // SAVE CATEGORY
    // ==========================

    @PostMapping("/save")
    public String save(Category category) {

        categoryService.saveCategory(category);

        return "redirect:/admin/categories";

    }

    // ==========================
    // EDIT CATEGORY
    // ==========================

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model) {

        model.addAttribute("categories",
                categoryService.getAllCategories());

        model.addAttribute("category",
                categoryService.getCategory(id));

        return "admin-categories";

    }

    // ==========================
    // DELETE CATEGORY
    // ==========================

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        categoryService.deleteCategory(id);

        return "redirect:/admin/categories";

    }

    // ==========================
    // SEARCH CATEGORY
    // ==========================

    @GetMapping("/search")
    public String search(
            @RequestParam String keyword,
            Model model) {

        model.addAttribute("categories",
                categoryService.search(keyword));

        model.addAttribute("category",
                new Category());

        return "admin-categories";

    }

}