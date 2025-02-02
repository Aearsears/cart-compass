import boto3


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
        table = self.client.describe_table(
            TableName=self.table_name).get("Table")

        if not table:
            self.create_table()

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
        ])

    def put(self, item):
        self.client.put_item(TableName=self.table_name, **item)
