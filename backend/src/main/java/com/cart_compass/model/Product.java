package com.cart_compass.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class Product {
    private String UPC;
    private String Date;
    private String Category;
    private String Brand;
    private String User_Friendly_Product_Name;
    private String Price;
    private String Unit;

    @DynamoDbPartitionKey
    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    @DynamoDbSortKey
    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    @DynamoDbAttribute("Category")
    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    @DynamoDbAttribute("Brand")
    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }

    @DynamoDbAttribute("User_Friendly_Product_Name")
    public String getUserFriendlyProductName() {
        return User_Friendly_Product_Name;
    }

    public void setUserFriendlyProductName(String User_Friendly_Product_Name) {
        this.User_Friendly_Product_Name = User_Friendly_Product_Name;
    }

    @DynamoDbAttribute("Price")
    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    @DynamoDbAttribute("Unit")
    public String getUnit() {
        return Unit;
    }

    public void setUnit(String Unit) {
        this.Unit = Unit;
    }
}
