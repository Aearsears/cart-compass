import logging
import random
from datetime import datetime
import time
from playwright.async_api import async_playwright
import asyncio

from scraper.DynamoDBClient import DynamoDBClient
from scraper.Settings import Settings

# TODO: initial seeding from urls https://www.superc.ca/en/aisles/*
URLS = [
    "https://www.superc.ca/en/aisles/meat-poultry/beef-veal/ground/extra-lean-ground-beef/p/201024",
    "https://www.superc.ca/en/aisles/meat-poultry/beef-veal/ground/lean-ground-beef/p/201758",
    "https://www.superc.ca/en/aisles/beverages/tea-hot-drinks/green-tea/green-tea-bags/p/059749983853",
    "https://www.superc.ca/en/aisles/dairy-eggs/milk-cream-butter/butter-margarine/salted-butter/p/059749894784",
    "https://www.superc.ca/en/aisles/dairy-eggs/packaged-cheese/shredded-cheese/shredded-cheddar-and-pizza-mozzarella-cheese-blend/p/068200477367",
    "https://www.superc.ca/en/aisles/dairy-eggs/packaged-cheese/shredded-cheese/shredded-mozzarella-cheese/p/059749949316",
    "https://www.superc.ca/en/aisles/fruits-vegetables/fruits/apples-pears/apple/p/4152",
    "https://www.superc.ca/en/aisles/fruits-vegetables/fruits/berries-cherries/blueberries/p/033383222288",
    "https://www.superc.ca/en/aisles/bread-bakery-products/freshly-baked-bread-baguettes/premiere-moisson/old-fashioned-belgian-loaf/p/029145099519"
]

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s')


async def scrape(url, browser, ddbclient, settings):
    logging.info(f"Visiting {url}")
    context = await browser.new_context()
    await context.set_extra_http_headers({"User-Agent": random.choice(settings.user_agents)})
    page = await context.new_page()
    await asyncio.sleep(random.randint(2, 10))
    await page.goto(url)
    try:
        await page.wait_for_load_state("networkidle", timeout=30000)
    except Exception as e:
        # TODO: implement retry logic
        logging.error(f"Error loading page {url}: {e}")
        await page.close()
        await context.close()
        return

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
    item = {
        "UPC": {
            "S": upc
        },
        "Date": {
            "S": datetime.now().isoformat()
        },
        "Category": {
            "S": category
        },
        "Brand": {
            "S": brand
        },
        "User_Friendly_Product_Name": {
            "S": user_friendly_product_name
        },
        "Price": {
            "N": price
        },
        "Unit": {
            "S": unit
        }
    }
    ddbclient.put(item)

    await page.close()
    await context.close()


async def worker(queue, browser, ddbclient, settings):
    while True:
        url = await queue.get()
        await scrape(url, browser, ddbclient, settings)
        queue.task_done()


async def main(urls, ddbclient, settings):
    queue = asyncio.Queue()
    for url in urls:
        await queue.put(url)

    # store in ddb list of urls already scraped for the day
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        workers = [asyncio.create_task(worker(queue, browser, ddbclient, settings))
                   for _ in range(2)]
        await queue.join()
        for w in workers:
            w.cancel()
        await browser.close()

logging.info("Starting scraper")
settings = Settings()
ddbclient = DynamoDBClient(
    region=settings.region,
    endpoint=settings.endpoint_url,
    access_key=settings.aws_access_key_id,
    secret=settings.aws_secret_access_key
)
# TODO: perform full scan on ddb table to get all urls
asyncio.run(main(URLS, ddbclient, settings))
logging.info("Finished scraper")
