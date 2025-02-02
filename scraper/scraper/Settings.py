from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    region: str
    endpoint_url: str
    aws_access_key_id: str
    aws_secret_access_key: str

    model_config = SettingsConfigDict(
        env_file='.env', env_file_encoding='utf-8')
