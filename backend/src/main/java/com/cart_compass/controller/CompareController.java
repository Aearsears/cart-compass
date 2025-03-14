package com.cart_compass.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private static final Logger logger = LoggerFactory.getLogger(CompareController.class);

    @Autowired
    public CompareController(ProductService productService) {
        this.productService = productService;
    }

    // Get a product by UPC
    @GetMapping("")
    public ResponseEntity<List<Product>> compareSingleProductByUPC(@RequestParam("upc") String upc) {
        List<Product> product = productService.getPricesByUPC(upc);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Compare multiple products and return a map of supermarket to their total cost
    // Example: /compare/basket?upcs=4152
    // sortByProduct is a flag to sort the products by their cheapest price and its
    // corresponding supermarket
    @GetMapping("/basket")
    public ResponseEntity<Map<String, Double>> compareBasket(@RequestParam("upcs") String[] products,
            @RequestParam Optional<Boolean> sortByProduct) {
        Map<String, Double> prices = productService.calculateTotalCost(new ArrayList<String>(Arrays.asList(products)));
        if (prices != null) {
            return ResponseEntity.ok(prices);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
