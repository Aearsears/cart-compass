package com.cart_compass.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cart_compass.model.Product;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;

// @Service
// public class ProductService {
// private final DynamoDbTable<Product> productTable;

// @Autowired
// public ProductService(DynamoDbEnhancedClient dynamoDbEnchancedClient) {
// this.productTable = dynamoDbEnchancedClient.table("ProductPrices",
// TableSchema.fromBean(Product.class));
// }

// }
