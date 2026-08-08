package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.SubCategory;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.SubCategoryService;

@Controller
public class AdminSubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private CategoryService categoryService;

    // ==========================
    // LIST SUB CATEGORIES
    // ==========================

    @GetMapping("/admin/sub-categories")
    public String subCategories(Model model) {

        model.addAttribute("subCategories",
                subCategoryService.getAllSubCategories());

        model.addAttribute("categories",
                categoryService.getAllCategories());

        model.addAttribute("subCategory",
                new SubCategory());

        return "admin-sub-categories";
    }

    // ==========================
    // SAVE SUB CATEGORY
    // ==========================

    @PostMapping("/admin/sub-categories/save")
    public String save(

            @RequestParam String name,

            @RequestParam Long categoryId,

            @RequestParam(required = false) String image,

            @RequestParam(defaultValue = "true") Boolean active) {

        Category category =
                categoryService.getCategory(categoryId);

        if (category == null) {

            return "redirect:/admin/sub-categories";
        }

        SubCategory subCategory = new SubCategory();

        subCategory.setName(name);

        subCategory.setCategory(category);

        subCategory.setImage(image);

        subCategory.setActive(active);

        subCategoryService.save(subCategory);

        return "redirect:/admin/sub-categories";
    }

    // ==========================
    // EDIT SUB CATEGORY
    // ==========================

    @GetMapping("/admin/sub-categories/edit/{id}")
    public String edit(

            @PathVariable Long id,

            Model model) {

        model.addAttribute("subCategory",
                subCategoryService.getSubCategory(id));

        model.addAttribute("categories",
                categoryService.getAllCategories());

        model.addAttribute("subCategories",
                subCategoryService.getAllSubCategories());

        return "admin-sub-categories";
    }

    // ==========================
    // UPDATE SUB CATEGORY
    // ==========================

    @PostMapping("/admin/sub-categories/update")
    public String update(

            @RequestParam Long id,

            @RequestParam String name,

            @RequestParam Long categoryId,

            @RequestParam(required = false) String image,

            @RequestParam(defaultValue = "true") Boolean active) {

        Category category =
                categoryService.getCategory(categoryId);

        SubCategory subCategory =
                new SubCategory();

        subCategory.setName(name);

        subCategory.setCategory(category);

        subCategory.setImage(image);

        subCategory.setActive(active);

        subCategoryService.update(id, subCategory);

        return "redirect:/admin/sub-categories";
    }

    // ==========================
    // DELETE
    // ==========================

    @GetMapping("/admin/sub-categories/delete/{id}")
    public String delete(
            @PathVariable Long id) {

        subCategoryService.delete(id);

        return "redirect:/admin/sub-categories";
    }

    // ==========================
    // SEARCH
    // ==========================

    @GetMapping("/admin/sub-categories/search")
    public String search(

            @RequestParam String keyword,

            Model model) {

        List<SubCategory> results =
                subCategoryService.search(keyword);

        model.addAttribute("subCategories", results);

        model.addAttribute("categories",
                categoryService.getAllCategories());

        model.addAttribute("subCategory",
                new SubCategory());

        return "admin-sub-categories";
    }

}