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
@RequestMapping("/supermarket")
public class SupermarketController {
    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(SupermarketController.class);

    @Autowired
    public SupermarketController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Search by supermarket's name and return all of its products
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchBySupermarketName(
            @RequestParam String name) {

        List<Product> results = productService.getProductsBySupermarketName(name);
        return ResponseEntity.ok(results);
    }

}