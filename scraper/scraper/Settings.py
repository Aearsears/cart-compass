from typing import List
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    region: str
    endpoint_url: str
    aws_access_key_id: str
    aws_secret_access_key: str
    user_agents: List[str] = [
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36",
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
    ]
    initial_seeds: List[str] = [
        "https://www.metro.ca/en/online-grocery/aisles",
        "https://www.superc.ca/en/aisles",
        "https://www.maxi.ca/en/food/c/27985",
        "https://www.provigo.ca/en/food/c/27985"
    ]
    model_config = SettingsConfigDict(
        env_file='.env', env_file_encoding='utf-8')
