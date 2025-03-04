package com.cart_compass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @RequestMapping("/")
    public String home() {
        return "Hello, welcome to Cart Compass!";
    }
}