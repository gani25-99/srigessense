package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.SubCategory;
import com.ecommerce.service.FileUploadService;
import com.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUploadService fileUploadService;

    // ==========================
    // GET ALL PRODUCTS
    // ==========================

    @GetMapping
    public List<Product> getAllProducts() {

        return productService.getAllProducts();

    }

    // ==========================
    // GET PRODUCT BY ID
    // ==========================

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {

        return productService.getProduct(id);

    }

    // ==========================
    // SAVE PRODUCT (JSON)
    // ==========================

    @PostMapping
    public Product save(@RequestBody Product product) {

        return productService.save(product);

    }

    // ==========================
    // UPLOAD PRODUCT
    // ==========================

    @PostMapping("/upload")
    public Product uploadProduct(

            @RequestParam("name") String name,

            @RequestParam("description") String description,

            @RequestParam("price") Double price,

            @RequestParam("quantity") Integer quantity,

            @RequestParam("categoryId") Long categoryId,

            @RequestParam("subcategoryId") Long subCategoryId,

            @RequestParam("image") MultipartFile image)

            throws Exception {

        String fileName =
                fileUploadService.uploadFile(image);

        Category category = new Category();

        category.setId(categoryId);

        SubCategory subCategory = new SubCategory();

        subCategory.setId(subCategoryId);

        Product product = new Product();

        product.setName(name);

        product.setDescription(description);

        product.setPrice(price);

        product.setQuantity(quantity);

        product.setImage(fileName);

        product.setCategory(category);

        product.setSubCategory(subCategory);

        return productService.save(product);

    }

    // ==========================
    // UPDATE PRODUCT
    // ==========================

    @PutMapping("/{id}")
    public Product update(

            @PathVariable Long id,

            @RequestBody Product product) {

        return productService.update(id, product);

    }

    // ==========================
    // DELETE PRODUCT
    // ==========================

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        try {

            productService.delete(id);

            return "Product Deleted Successfully";

        }

        catch (Exception e) {

            throw new ResponseStatusException(

                    HttpStatus.BAD_REQUEST,

                    "Cannot delete product. Remove it from Cart, Wishlist and Orders first."

            );

        }

    }

    // ==========================
    // SEARCH PRODUCTS
    // ==========================

    @GetMapping("/search")
    public List<Product> search(

            @RequestParam String keyword) {

        return productService.search(keyword);

    }

}