package com.cart_compass.controller;

import com.cart_compass.model.Product;
import com.cart_compass.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.ok(product);
    }

    // Get a product by UPC
    @GetMapping("/{upc}")
    public ResponseEntity<Product> getProductByUPC(@PathVariable String upc) {
        Product product = productService.getProductByUPC(upc);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing product
    @PutMapping("/{upc}")
    public ResponseEntity<Product> updateProduct(@PathVariable String upc, @RequestBody Product product) {
        Product existingProduct = productService.getProductByUPC(upc);
        if (existingProduct != null) {
            productService.updateProduct(product);
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a product by UPC
    @DeleteMapping("/{upc}")
    public ResponseEntity<Void> deleteProductByUPC(@PathVariable String upc) {
        Product existingProduct = productService.getProductByUPC(upc);
        if (existingProduct != null) {
            productService.deleteProductByUPC(upc);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        // TODO: fix scrape date null
        // TODO: fix java.lang.ClassCastException
        List<Product> products = productService.scanTable();
        System.out.println(products.get(0).getScrapeDate());
        // for (Product product : products) {
        // System.out.println(product);
        // }
        return ResponseEntity.ok(products);
    }
}