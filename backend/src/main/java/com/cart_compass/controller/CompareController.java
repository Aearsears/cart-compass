package com.cart_compass.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cart_compass.model.Product;
import com.cart_compass.service.ProductService;

@RestController
@RequestMapping("/compare")
public class CompareController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public CompareController(ProductService productService) {
        this.productService = productService;
    }

    // Get a product by UPC
    @GetMapping("/{upc}")
    public ResponseEntity<List<Product>> compareSingleProductByUPC(@PathVariable String upc) {
        List<Product> product = productService.getPricesByUPC(upc);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/compare/basket")
    void compareBasket(@RequestParam("products") String[] products) {
        /*
         * Example: /compare/basket?products=milk,bread,eggs
         */
    }
}
