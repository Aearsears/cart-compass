import requests
from bs4 import BeautifulSoup
import logging
import random
import time


def get_url(url, headers={}):

    headers.update({"User-Agent": "Mozilla/5.0"})

    jitter = random.randint(1, 10)
    time.sleep(jitter)

    return requests.get(url, headers=headers)


logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s')


# store in ddb list of urls already scraped for the day
def scrape_website(url):
    try:
        # Send a GET request to the website
        response = get_url(url)
        response.raise_for_status()  # Raise an error for bad status codes

        # Parse the HTML content using BeautifulSoup
        soup = BeautifulSoup(response.text, 'html.parser')

        # Extract and print the title of the webpage
        title = soup.title.string if soup.title else "No title found"
        print(f"Page Title: {title}")

        # Example: Extract all links from the page
        links = [a['href'] for a in soup.find_all('a', href=True)]
        print("Links found:")
        for link in links[:10]:  # Print first 10 links as a sample
            print(link)

    except requests.exceptions.RequestException as e:
        print(f"Error fetching {url}: {e}")


if __name__ == "__main__":
    logging.info("Starting scraper")
    website_url = "https://google.com"  # Change this to your target URL
    scrape_website(website_url)
    logging.info("Finished scraper")
