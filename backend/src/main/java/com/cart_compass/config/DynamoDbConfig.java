package com.cart_compass.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClientExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient.Builder;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import io.micrometer.common.lang.NonNull;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.dynamodb.accessKey}")
    private String accessKey;

    @Value("${aws.dynamodb.secretKey}")
    private String secretKey;

    @Value("${aws.dynamodb.endpoint}")
    private String endpoint;

    @Bean
    public DynamoDbClient getDynamoDbClient() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDbClient())
                .build();
    }

    @Bean
    public DynamoDbTableNameResolver tableNameResolver() {
        return new CustomTableNameResolver();
    }

}

class CustomTableNameResolver implements DynamoDbTableNameResolver {

    @Override
    public <T> String resolve(@NonNull Class<T> clazz) {
        if (clazz.equals(com.cart_compass.model.Product.class)) {
            return "ProductPrices";
        }
        return "UnknownTable";
    }
}