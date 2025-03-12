package com.cart_compass.controller;

import org.springframework.web.bind.annotation.*;

import com.cart_compass.model.Product;
import com.cart_compass.service.ProductService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Search products by name with optional price range filter.
     */
    @GetMapping("/product")
    public ResponseEntity<List<Product>> searchByProductName(
            @RequestParam String productName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        List<Product> results = productService.searchByProductName(productName);
        return ResponseEntity.ok(results);
    }

    /**
     * Search products by category with optional price and unit type filters.
     */
    @GetMapping("/category")
    public ResponseEntity<List<Product>> filterByCategory(
            @RequestParam String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String unitType) {

        List<Product> results = productService.searchByCategory(category);
        return ResponseEntity.ok(results);
    }
}
