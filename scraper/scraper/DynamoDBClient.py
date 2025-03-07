import logging
import time
import boto3
from botocore.exceptions import ClientError

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s')


class DynamoDBClient():
    def __init__(self, *, region, endpoint, access_key, secret) -> None:
        self.table_name = "ProductPrices"
        self.client = boto3.client(
            'dynamodb',
            region_name=region,
            endpoint_url=endpoint,
            aws_access_key_id=access_key,
            aws_secret_access_key=secret
        )
        self.GSIS = [
            {
                "IndexName": "UserFriendlyProductNameIndex",
                "KeySchema": [
                    {"AttributeName": "User_Friendly_Product_Name", "KeyType": "HASH"},
                    {"AttributeName": "UPC", "KeyType": "RANGE"},
                ],
                "Projection": {"ProjectionType": "ALL"},
            },
            {
                "IndexName": "CategoryIndex",
                "KeySchema": [
                    {"AttributeName": "Category", "KeyType": "HASH"},
                    {"AttributeName": "UPC", "KeyType": "RANGE"},
                ],
                "Projection": {"ProjectionType": "ALL"},
            }
        ]
        if not self.table_exists():
            self.create_table()
            self.wait_for_table_active()

        self.create_missing_gsis()

    def get_existing_gsis(self):
        """Check existing GSIs on the DynamoDB table"""
        try:
            response = self.client.describe_table(TableName=self.table_name)
            existing_gsis = response["Table"].get("GlobalSecondaryIndexes", [])
            return [gsi["IndexName"] for gsi in existing_gsis]
        except Exception as e:
            logging.info(f"Error fetching table details: {e}")
            return []

    def create_missing_gsis(self):
        """Create missing GSIs if they do not exist"""
        existing_gsis = self.get_existing_gsis()

        # Find which GSIs need to be created
        missing_gsis = [
            gsi for gsi in self.GSIS if gsi["IndexName"] not in existing_gsis]

        if not missing_gsis:
            logging.info(
                "✅ All required GSIs already exist. No changes needed.")
            return

        logging.info(
            f"🚀 Creating missing GSIs: {[gsi['IndexName'] for gsi in missing_gsis]}")

        # Define required attribute definitions
        attribute_definitions = {
            "UserFriendlyProductNameIndex": [
                {"AttributeName": "User_Friendly_Product_Name", "AttributeType": "S"},
                {"AttributeName": "UPC", "AttributeType": "S"}
            ],
            "CategoryIndex": [
                {"AttributeName": "Category", "AttributeType": "S"},
                {"AttributeName": "UPC", "AttributeType": "S"}
            ]
        }

        logging.info(
            "⌛ GSIs are being created... This may take several minutes.")
        for gsi in missing_gsis:
            response = self.client.update_table(
                TableName=self.table_name,
                AttributeDefinitions=attribute_definitions[gsi["IndexName"]],
                GlobalSecondaryIndexUpdates=[{"Create": gsi}],
            )
            self.wait_for_table_active()

    def table_exists(self):
        """Checks if the table exists by describing it."""
        try:
            response = self.client.describe_table(TableName=self.table_name)
            return "Table" in response
        except ClientError as e:
            if e.response["Error"]["Code"] == "ResourceNotFoundException":
                return False  # Table does not exist
            raise  # Some other unexpected error

    def wait_for_table_active(self):
        """Waits until the table is in ACTIVE state."""
        while True:
            try:
                response = self.client.describe_table(
                    TableName=self.table_name)
                status = response["Table"]["TableStatus"]
                if status == "ACTIVE":
                    logging.info("Table is now active.")
                    break
                else:
                    logging.info(
                        f"Waiting for table to become ACTIVE (current status: {status})...")
            except ClientError as e:
                if e.response["Error"]["Code"] != "ResourceNotFoundException":
                    raise
            time.sleep(2)  # Polling interval

    def create_table(self):
        try:
            self.client.create_table(AttributeDefinitions=[
                {
                    'AttributeName': 'UPC',
                    'AttributeType': 'S'
                },
                {
                    'AttributeName': 'ScrapeDate',
                    'AttributeType': 'S'
                },
            ],
                TableName=self.table_name,
                KeySchema=[
                {
                    'AttributeName': 'UPC',
                    'KeyType': 'HASH'
                },
                {
                    'AttributeName': 'ScrapeDate',
                    'KeyType': 'RANGE'
                },
            ],
                BillingMode="PAY_PER_REQUEST")
        except ClientError as e:
            if e.response["Error"]["Code"] == "ResourceInUseException":
                logging.info(
                    "Table is already being created by another process.")
            else:
                raise

    def put(self, item):
        self.client.put_item(
            TableName=self.table_name, Item=item)
