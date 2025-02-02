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
        try:
            table = self.client.describe_table(
                TableName=self.table_name).get("Table")
        except ClientError as e:
            if e.response['Error']['Code'] == 'ResourceNotFoundException':
                self.create_table()
            else:
                print(f"Error: {e}")

    def create_table(self):
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
            TableName='string',
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

    def put(self, item):
        self.client.put_item(TableName=self.table_name, **item)
