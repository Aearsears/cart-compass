import asyncio
import logging
import queue
import random
from typing import Set
from playwright.async_api import async_playwright
from scraper.DynamoDBClient import DynamoDBClient
from scraper.Settings import Settings

logging.basicConfig(level=logging.INFO,
                    format='%(asctime)s - %(levelname)s - %(message)s')


async def worker(settings, q: asyncio.Queue, ddbclient, browser, visited: Set):
    while True:
        url = await q.get()
        if url in visited:
            q.task_done()
            continue
        if url is None:
            q.task_done()
            break
        visited.add(url)
        context = await browser.new_context()
        await context.set_extra_http_headers({"User-Agent": random.choice(settings.user_agents)})
        page = await context.new_page()
        await asyncio.sleep(random.randint(1, 10))
        await page.goto(url)
        await page.wait_for_load_state("networkidle")

        # Get all links on the page
        links = await page.evaluate('''() => 
            Array.from(document.querySelectorAll('a'))
                 .map(a => a.href)''')
        # TODO: if product page

        # Filter links that start with the given prefix
        filtered_links = [link for link in links if any(
            link.startswith(prefix) for prefix in settings.initial_seeds)]
        [await q.put(link) for link in filtered_links]
        await asyncio.sleep(1)
        logging.info(f"here is the url: {url}")
        q.task_done()


async def main():
    settings = Settings()
    ddbclient = DynamoDBClient(
        region=settings.region,
        endpoint=settings.endpoint_url,
        access_key=settings.aws_access_key_id,
        secret=settings.aws_secret_access_key
    )

    queue = asyncio.Queue()
    visited = set()
    [await queue.put(url) for url in settings.initial_seeds]

    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=True)
        workers = [asyncio.create_task(
            worker(settings, queue, ddbclient, browser, visited)) for _ in range(10)]
        logging.info("here")
        await queue.join()
        for _ in range(10):
            await queue.put(None)
        await asyncio.gather(*workers)
        await browser.close()


asyncio.run(main())
