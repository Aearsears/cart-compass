package com.cart_compass.config;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClientExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient.Builder;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;

// @Configuration
// public class DynamoDbConfig {

// @Bean
// public DynamoDbEnhancedClient enhancedClient(DynamoDbClient dynamoDbClient,
// DynamoDbTableNameResolver tableNameResolver) {
// Builder builder = DynamoDbEnhancedClient.builder()
// .dynamoDbClient(dynamoDbClient)
// .tableNameResolver(tableNameResolver);
// return builder.build();
// }

// @Bean
// public DynamoDbTableNameResolver tableNameResolver() {
// return new CustomTableNameResolver();
// }

// }

// class CustomTableNameResolver implements DynamoDbTableNameResolver {

// @Override
// public <T> TableSchema<T> resolveTableSchema(Class<T> clazz, TableSchema<T>
// tableSchema) {
// if (clazz.equals(com.cart_compass.model.Product.class)) {
// return
// TableSchema.fromBean(clazz).withTableNameOverride(TableNameOverride.withTableName("ProductPrices"));
// }
// return tableSchema;
// }
// }