import logging
import random
import time
from datetime import datetime
from playwright.async_api import async_playwright
import asyncio

from scraper.DynamoDBClient import DynamoDBClient
from scraper.Settings import Settings

# TODO: initial seeding from urls https://www.superc.ca/en/aisles/*
URLS = [
    "https://www.superc.ca/en/aisles/meat-poultry/beef-veal/ground/extra-lean-ground-beef/p/201024",
    "https://www.superc.ca/en/aisles/beverages/tea-hot-drinks/green-tea/green-tea-bags/p/059749983853",
    "https://www.superc.ca/en/aisles/dairy-eggs/milk-cream-butter/butter-margarine/salted-butter/p/059749894784"
]

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s')


async def scrape(url, browser, ddbclient):
    logging.info(f"Visiting {url}")
    context = await browser.new_context()
    await context.set_extra_http_headers({"User-Agent": random.choice(USER_AGENTS)})
    page = await context.new_page()
    await asyncio.sleep(random.randint(1, 10))
    await page.goto(url)
    await page.wait_for_load_state("networkidle")

    # TODO: load into ddb
    upc_element = await page.query_selector(
        'div[data-product-code]')
    upc = await upc_element.get_attribute('data-product-code')

    category_element = await page.query_selector(
        'div[data-product-category]')
    category = await category_element.get_attribute('data-product-category')

    brand_element = await page.query_selector(
        'div[data-product-brand]')
    if brand_element:
        brand = await brand_element.get_attribute('data-product-brand')
    else:
        brand = "N/A"

    user_friendly_product_name_element = await page.query_selector(
        'div[data-product-name]')
    user_friendly_product_name = await user_friendly_product_name_element.get_attribute(
        'data-product-name')

    price_element = await page.query_selector(
        'div[data-main-price]')
    price = await price_element.get_attribute('data-main-price')

    unit_element = await page.query_selector('div[data-variant-price]')
    if unit_element:
        unit = await unit_element.get_attribute('data-variant-price')
    else:
        unit = "each"

    print(f"{user_friendly_product_name} ({upc}) Brand: {
          brand} (Category: {category}) at {price}/{unit}")

    await context.close()


async def main(urls, ddbclient):
    # store in ddb list of urls already scraped for the day
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        await asyncio.gather(*(scrape(url, browser, ddbclient) for url in urls))
        await browser.close()

settings = Settings()
ddbclient = DynamoDBClient(
    region=settings.region,
    endpoint=settings.endpoint_url,
    access_key=settings.aws_access_key_id,
    secret=settings.aws_secret_access_key
)
# TODO: perform full scan on ddb table to get all urls
logging.info("Starting scraper")
asyncio.run(main(URLS, ddbclient))
logging.info("Finished scraper")
