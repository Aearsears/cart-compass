package com.cart_compass.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cart_compass.model.Product;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final DynamoDbTable<Product> productTable;
    private final DynamoDbIndex<Product> categoryIndex;
    private final DynamoDbIndex<Product> userFriendlyProductNameIndex;
    private final DynamoDbIndex<Product> UPCBySupermarketNameAndDateIndex;
    private final DynamoDbIndex<Product> supermarketIndex;

    @Autowired
    public ProductService(DynamoDbEnhancedClient dynamoDbEnchancedClient) {
        this.productTable = dynamoDbEnchancedClient.table("ProductPrices",
                TableSchema.fromBean(Product.class));
        this.categoryIndex = productTable.index("CategoryIndex");
        this.userFriendlyProductNameIndex = productTable.index("UserFriendlyProductNameIndex");
        this.UPCBySupermarketNameAndDateIndex = productTable.index("UPCBySupermarketNameAndDateIndex");
        this.supermarketIndex = productTable.index("SupermarketIndex");
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

    // Method to search for a product by name using the GSI
    // UserFriendlyProductNameIndex
    public List<Product> searchByProductName(String productName) {
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(productName)))
                .build();

        SdkIterable<Page<Product>> response = userFriendlyProductNameIndex.query(queryRequest);

        return response.stream().flatMap(page -> page.items().stream()).collect(Collectors.toList());
    }

    // Method to search for a product by name using the GSI CategoryIndex
    public List<Product> searchByCategory(String category) {
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(category)))
                .build();

        SdkIterable<Page<Product>> response = categoryIndex.query(queryRequest);

        return response.stream().flatMap(page -> page.items().stream()).collect(Collectors.toList());
    }

    // Search for prices of a product (by UPC) across different supermarkets
    public List<Product> getPricesByUPC(String upc) {
        // Query for the product by UPC
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(upc)))
                .scanIndexForward(false)
                .build();

        SdkIterable<Page<Product>> response = UPCBySupermarketNameAndDateIndex.query(queryRequest);

        return response.stream().flatMap(page -> page.items().stream()).collect(Collectors.toList());
    }

    // Search for products by supermarket name
    public List<Product> getProductsBySupermarketName(String name) {
        // Query for the product by UPC
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(name)))
                .scanIndexForward(false)
                .build();

        SdkIterable<Page<Product>> response = supermarketIndex.query(queryRequest);

        return response.stream().flatMap(page -> page.items().stream()).collect(Collectors.toList());
    }

    // Method to compare multiple products and return a map of supermarket to their
    // total cost
    public Map<String, Double> calculateTotalCost(List<String> upcs) {
        Map<String, List<Product>> supermarketProducts = new HashMap<>();
        Map<String, Double> supermarketTotals = new HashMap<>();

        for (String upc : upcs) {
            QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                    .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(upc)))
                    .build();

            SdkIterable<Page<Product>> response = UPCBySupermarketNameAndDateIndex.query(queryRequest);

            List<Product> products = response
                    .stream()
                    .flatMap(page -> page.items().stream())
                    .collect(Collectors.toList());

            for (Product p : products) {
                supermarketProducts.putIfAbsent(p.getSupermarketName(), new ArrayList<>());
                supermarketProducts.get(p.getSupermarketName()).add(p);
            }

        }
        // dedupe products
        for (String supermarket : supermarketProducts.keySet()) {
            List<Product> products = supermarketProducts.get(supermarket);
            Map<String, Product> dedupedProductsMin = new HashMap<>();
            for (Product product : products) {
                dedupedProductsMin.putIfAbsent(product.getUserFriendlyProductName(), product);
                if (dedupedProductsMin.get(product.getUserFriendlyProductName()).getScrapeDateDateTime()
                        .isBefore(product.getScrapeDateDateTime())) {
                    dedupedProductsMin.put(product.getUserFriendlyProductName(), product);
                }
            }
            supermarketProducts.put(supermarket, new ArrayList<>(dedupedProductsMin.values()));
        }
        for (Map.Entry<String, List<Product>> entry : supermarketProducts.entrySet()) {
            for (Product product : entry.getValue()) {
                supermarketTotals.put(
                        entry.getKey(),
                        supermarketTotals.getOrDefault(product.getSupermarketName(), 0.0)
                                + Double.parseDouble(product.getPrice()));
            }
        }

        return supermarketTotals;
    }

}
