package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;

@Controller
public class ProductPageController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ==========================
    // ALL PRODUCTS / SEARCH / BRAND
    // ==========================

    @GetMapping("/products")
    public String products(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) String brand,

            Model model) {

        if (keyword != null && !keyword.trim().isEmpty()) {

            model.addAttribute(
                    "products",
                    productRepository.searchProducts(keyword));

            model.addAttribute(
                    "keyword",
                    keyword);

        }

        else if (brand != null && !brand.trim().isEmpty()) {

            model.addAttribute(
                    "products",
                    productRepository.findByBrandIgnoreCase(brand));

            model.addAttribute(
                    "brand",
                    brand);

        }

        else {

            model.addAttribute(
                    "products",
                    productRepository.findAll());

        }

        model.addAttribute(
                "categories",
                categoryRepository.findAll());

        model.addAttribute(
                "brands",
                productRepository.findDistinctBrands());

        model.addAttribute(
                "title",
                "All Products");

        return "products";
    }

    // ==========================
    // CASUAL SHIRTS
    // ==========================

    @GetMapping("/shirts/casual")
    public String casualShirts(Model model) {

        loadProductsBySubCategory("Casual Shirts", model);

        return "products";
    }

    // ==========================
    // FORMAL SHIRTS
    // ==========================

    @GetMapping("/shirts/formal")
    public String formalShirts(Model model) {

        loadProductsBySubCategory("Formal Shirts", model);

        return "products";
    }

    // ==========================
    // OVERSIZED SHIRTS
    // ==========================

    @GetMapping("/shirts/oversized")
    public String oversizedShirts(Model model) {

        loadProductsBySubCategory("Oversized Shirts", model);

        return "products";
    }

    // ==========================
    // DENIM SHIRTS
    // ==========================

    @GetMapping("/shirts/denim")
    public String denimShirts(Model model) {

        loadProductsBySubCategory("Denim Shirts", model);

        return "products";
    }

    // ==========================
    // JEANS
    // ==========================

    @GetMapping("/jeans")
    public String jeans(Model model) {

        loadProductsBySubCategory("Jeans", model);

        return "products";
    }

    // ==========================
    // CHINOS
    // ==========================

    @GetMapping("/chinos")
    public String chinos(Model model) {

        loadProductsBySubCategory("Chinos", model);

        return "products";
    }

    // ==========================
    // CARGO PANTS
    // ==========================

    @GetMapping("/cargo-pants")
    public String cargoPants(Model model) {

        loadProductsBySubCategory("Cargo Pants", model);

        return "products";
    }

    // ==========================
    // FORMAL TROUSERS
    // ==========================

    @GetMapping("/formal-trousers")
    public String formalTrousers(Model model) {

        loadProductsBySubCategory("Formal Trousers", model);

        return "products";
    }

    // ==========================
    // SNEAKERS
    // ==========================

    @GetMapping("/sneakers")
    public String sneakers(Model model) {

        loadProductsBySubCategory("Sneakers", model);

        return "products";
    }

    // ==========================
    // SPORTS SHOES
    // ==========================

    @GetMapping("/sports-shoes")
    public String sportsShoes(Model model) {

        loadProductsBySubCategory("Sports Shoes", model);

        return "products";
    }

    // ==========================
    // FORMAL SHOES
    // ==========================

    @GetMapping("/formal-shoes")
    public String formalShoes(Model model) {

        loadProductsBySubCategory("Formal Shoes", model);

        return "products";
    }

    // ==========================
    // LOAFERS
    // ==========================

    @GetMapping("/loafers")
    public String loafers(Model model) {

        loadProductsBySubCategory("Loafers", model);

        return "products";
    }

    // ==========================
    // PERFUMES
    // ==========================

    @GetMapping("/perfumes")
    public String perfumes(Model model) {

        loadProductsBySubCategory("Perfumes", model);

        return "products";
    }

    // ==========================
    // HAIR CARE
    // ==========================

    @GetMapping("/hair-care")
    public String hairCare(Model model) {

        loadProductsBySubCategory("Hair Care", model);

        return "products";
    }

    // ==========================
    // FACE CARE
    // ==========================

    @GetMapping("/face-care")
    public String faceCare(Model model) {

        loadProductsBySubCategory("Face Care", model);

        return "products";
    }

    // ==========================
    // WATCHES
    // ==========================

    @GetMapping("/watches")
    public String watches(Model model) {

        loadProductsBySubCategory("Watches", model);

        return "products";
    }

    // ==========================
    // WALLETS
    // ==========================

    @GetMapping("/wallets")
    public String wallets(Model model) {

        loadProductsBySubCategory("Wallets", model);

        return "products";
    }

    // ==========================
    // BELTS
    // ==========================

    @GetMapping("/belts")
    public String belts(Model model) {

        loadProductsBySubCategory("Belts", model);

        return "products";
    }

    // ==========================
    // CAPS
    // ==========================

    @GetMapping("/caps")
    public String caps(Model model) {

        loadProductsBySubCategory("Caps", model);

        return "products";
    }

    // ==========================
    // SUNGLASSES
    // ==========================

    @GetMapping("/sunglasses")
    public String sunglasses(Model model) {

        loadProductsBySubCategory("Sunglasses", model);

        return "products";
    }

    // ==========================
    // COMMON METHOD
    // ==========================

    private void loadProductsBySubCategory(String subCategory, Model model) {

        model.addAttribute(
                "products",
                productRepository.findBySubCategoryNameIgnoreCase(subCategory));

        model.addAttribute(
                "categories",
                categoryRepository.findAll());

        model.addAttribute(
                "brands",
                productRepository.findDistinctBrands());

        model.addAttribute(
                "title",
                subCategory);
    }

}