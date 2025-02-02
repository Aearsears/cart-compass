import boto3


class DynamoDBClient():
    def __init__(self, *, region, endpoint, access_key, secret) -> None:
        self.client = boto3.client(
            'dynamodb',
            region_name=region,
            endpoint_url=endpoint,
            aws_access_key_id=access_key,
            aws_secret_access_key=secret
        )
