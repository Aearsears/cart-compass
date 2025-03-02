package com.cart_compass.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cart_compass.model.Product;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;

@Service
public class ProductService {
    private final DynamoDbTable<Product> productTable;

    @Autowired
    public ProductService(DynamoDbEnhancedClient dynamoDbEnchancedClient) {
        this.productTable = dynamoDbEnchancedClient.table("ProductPrices",
                TableSchema.fromBean(Product.class));
    }

    // Method to add a product
    public void addProduct(Product product) {
        productTable.putItem(product);
    }

    // Method to get a product by UPC
    public Product getProductByUPC(String upc) {
        return productTable.getItem(r -> r.key(k -> k.partitionValue(upc)));
    }

    // Method to update a product
    public void updateProduct(Product product) {
        productTable.updateItem(product);
    }

    // Method to delete a product by UPC
    public void deleteProductByUPC(String upc) {
        Key key = Key.builder().partitionValue(upc).build();

        DeleteItemEnhancedRequest deleteItemEnhancedRequest = DeleteItemEnhancedRequest.builder()
                .key(key)
                .build();

        productTable.deleteItem(deleteItemEnhancedRequest);
    }

    // Method to scan the table and return all items
    public List<Product> scanTable() {
        return productTable.scan().items().stream().collect(Collectors.toList());
    }
}
