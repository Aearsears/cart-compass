# Scraper

## DynamoDB model

Primary Key

-   Parition Key: UPC
-   Sort Key: ScrapeDate

Local Secondary Index UPCBySupermarketNameAndDateIndex

-   Parition Key: UPC
-   Sort Key: SupermarketName#ScrapeDate

TODO: update the sort keys to be more useful in search
GSI UserFriendlyProductNameIndex

-   Parition Key: User_Friendly_Product_Name
-   Sort Key: UPC

GSI CategoryIndex

-   Parition Key: Category
-   Sort Key: UPC

Attributes

-   Price
-   SupermarketName
-   User friendly product name
-   Category
-   Brand
-   URL
-   Unit (per lb or per container)

## Query patterns

-   Get UPC by date
-   Get UPC sorted by date ascending
