package com.cart_compass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cart_compass.service.ScraperService;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class DefaultController {
    private final ScraperService scraperService;
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    public DefaultController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @RequestMapping("/")
    public String home() {
        return "Hello, welcome to Cart Compass!";
    }

    @RequestMapping("/scrape")
    public String scrape() {
        try {
            scraperService.runPythonScript();
            return "Scraping started!";
        } catch (Exception e) {
            logger.error("Error starting scraping: ", e);
            return "Error starting scraping.";
        }
    }
}