package com.cart_compass.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompareController {

    // TODO: add table repository

    @GetMapping("/compare")
    void compareSingleProduct(@RequestParam("product") String product) {
        /*
         * example: /compare?product={productName}
         */
    }

    //
    @GetMapping("/compare/basket")
    void compareBasket(@RequestParam("products") String[] products) {
        /*
         * Example: /compare/basket?products=milk,bread,eggs
         */
    }
}
