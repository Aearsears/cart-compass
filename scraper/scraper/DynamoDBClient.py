import time
import boto3
from botocore.exceptions import ClientError


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
        if not self.table_exists():
            self.create_table()
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
                    print("Table is now active.")
                    break
                else:
                    print(
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
                    'AttributeName': 'Date',
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
                    'AttributeName': 'Date',
                    'KeyType': 'RANGE'
                },
            ],
                BillingMode="PAY_PER_REQUEST")
        except ClientError as e:
            if e.response["Error"]["Code"] == "ResourceInUseException":
                print("Table is already being created by another process.")
            else:
                raise

    def put(self, item):
        self.client.put_item(TableName=self.table_name, **item)
