package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ecommerce.service.ProductService;
import com.ecommerce.service.SubCategoryService;

@Controller
public class CustomerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SubCategoryService subCategoryService;

    // ==========================
    // PRODUCTS BY SUB CATEGORY
    // ==========================

    @GetMapping("/products/subcategory/{id}")
    public String productsBySubCategory(@PathVariable Long id,
                                        Model model) {

        model.addAttribute(
                "products",
                productService.getProductsBySubCategory(id));

        model.addAttribute(
                "title",
                subCategoryService.getSubCategory(id).getName());

        return "products";
    }
    // ==========================
// PRODUCT DETAILS
// ==========================

@GetMapping("/product/{id}")
public String productDetails(@PathVariable Long id,
                             Model model) {

    model.addAttribute(
            "product",
            productService.getProduct(id));

    return "product-details";
}

}