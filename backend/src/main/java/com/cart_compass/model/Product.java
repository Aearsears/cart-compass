package com.cart_compass.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import com.fasterxml.jackson.annotation.JsonProperty;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class Product {
    private String UPC;
    private String ScrapeDate;
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
    @DynamoDbAttribute("ScrapeDate")
    public String getScrapeDate() {
        return ScrapeDate;
    }

    public void setScrapeDate(String ScrapeDate) {
        this.ScrapeDate = ScrapeDate;
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

    @Override
    public String toString() {
        return "Product [UPC=" + UPC + ", ScrapeDate=" + ScrapeDate + ", Category=" +
                Category + ", Brand=" + Brand
                + ", User_Friendly_Product_Name=" + User_Friendly_Product_Name + ", Price=" +
                Price + ", Unit=" + Unit
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((UPC == null) ? 0 : UPC.hashCode());
        result = prime * result + ((ScrapeDate == null) ? 0 : ScrapeDate.hashCode());
        result = prime * result + ((Category == null) ? 0 : Category.hashCode());
        result = prime * result + ((Brand == null) ? 0 : Brand.hashCode());
        result = prime * result + ((User_Friendly_Product_Name == null) ? 0 : User_Friendly_Product_Name.hashCode());
        result = prime * result + ((Price == null) ? 0 : Price.hashCode());
        result = prime * result + ((Unit == null) ? 0 : Unit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (UPC == null) {
            if (other.UPC != null)
                return false;
        } else if (!UPC.equals(other.UPC))
            return false;
        if (ScrapeDate == null) {
            if (other.ScrapeDate != null)
                return false;
        } else if (!ScrapeDate.equals(other.ScrapeDate))
            return false;
        if (Category == null) {
            if (other.Category != null)
                return false;
        } else if (!Category.equals(other.Category))
            return false;
        if (Brand == null) {
            if (other.Brand != null)
                return false;
        } else if (!Brand.equals(other.Brand))
            return false;
        if (User_Friendly_Product_Name == null) {
            if (other.User_Friendly_Product_Name != null)
                return false;
        } else if (!User_Friendly_Product_Name.equals(other.User_Friendly_Product_Name))
            return false;
        if (Price == null) {
            if (other.Price != null)
                return false;
        } else if (!Price.equals(other.Price))
            return false;
        if (Unit == null) {
            if (other.Unit != null)
                return false;
        } else if (!Unit.equals(other.Unit))
            return false;
        return true;
    }

}
