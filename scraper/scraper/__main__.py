import requests
from bs4 import BeautifulSoup
import logging
import random
import time
from playwright.sync_api import sync_playwright

USER_AGENTS = [
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36",
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
]


def get_url(url, headers={}):

    headers.update(
        {
            "User-Agent": random.choice(USER_AGENTS),
            "Referer": "https://www.google.com/",
            "Accept-Language": "en-US,en;q=0.9"
        })

    jitter = random.randint(1, 10)
    time.sleep(jitter)

    session = requests.Session()
    session.headers.update(headers)
    return session.get(url)


logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s')


# store in ddb list of urls already scraped for the day
def scrape_website(url):
    try:
        response = get_url(url)
        response.raise_for_status()

        soup = BeautifulSoup(response.text, 'html.parser')

        title = soup.title.string if soup.title else "No title found"
        print(f"Page Title: {title}")

        price = soup.select_one('div').get('data-main-price')
        unit = soup.select_one('div').get('data-variant-price')
        print(f"{title} at {price}/{unit}")

    except requests.exceptions.RequestException as e:
        print(f"Error fetching {url}: {e}")


if __name__ == "__main__":
    logging.info("Starting scraper")
    # Change this to your target URL
    # website_url = "https://www.superc.ca/en/aisles/meat-poultry/beef-veal/ground/extra-lean-ground-beef/p/201024"
    # scrape_website(website_url)

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        page = browser.new_page(user_agent=random.choice(USER_AGENTS))

        page.goto(
            "https://www.superc.ca/en/aisles/meat-poultry/beef-veal/ground/extra-lean-ground-beef/p/201024")

        page.wait_for_load_state("networkidle")

        user_friendly_product_name = page.query_selector(
            'div[data-product-name]').get_attribute('data-product-name')
        price = page.query_selector(
            'div[data-main-price]').get_attribute('data-main-price')
        unit = page.query_selector(
            'div[data-variant-price]').get_attribute('data-variant-price')

        print(f"{user_friendly_product_name} at {price}/{unit}")

        browser.close()

    logging.info("Finished scraper")
