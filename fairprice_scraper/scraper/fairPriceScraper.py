# scraper.py

# scraper.py

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait 
from selenium.webdriver.support import expected_conditions as EC 
import pandas as pd
import time

def scrape(ingredient, min_price, max_price):
    options = Options()
    options.binary_location = "/usr/bin/chromium" #
    options.add_argument("--headless")
    options.add_argument("--no-sandbox")
    options.add_argument("--disable-dev-shm-usage")
    
    service = Service("/usr/bin/chromedriver")#
    driver = webdriver.Chrome(service=service,options=options)
    #driver = webdriver.Chrome(options=options)
    driver.set_page_load_timeout(25)

    try:
        # Go to FairPrice
        # url = f"https://www.fairprice.com.sg/search?query={ingredient}"
        # driver.get(url)
        # time.sleep(2)

        url  = 'https://www.fairprice.com.sg'
        driver.get(url)

        # manually interact with the search bar like a user
        search_bar = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, '#search-input-bar'))
        )
        search_bar.clear()
        search_bar.send_keys(ingredient)
        time.sleep(1)
        search_bar.send_keys(Keys.RETURN)

        # Wait up to 20 seconds for results
        WebDriverWait(driver, 20).until(
           EC.presence_of_element_located((By.CSS_SELECTOR, 'div.product-container'))

        )
        #print("✅ Products loaded.")

        for _ in range(5):
            driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
            time.sleep(2)

        product_name = []
        product_price = []
        product_url = []
        in_stock = []
        offer_discount = []
        image_url = []

        products = driver.find_elements(By.CSS_SELECTOR, 'div.product-container')

        for product in products:
            try:
                name = product.find_element(By.CSS_SELECTOR, 'span.gpnjpI').text.strip()
                name_lower = name.lower()
                ingredient_lower = ingredient.lower()
                keyword_parts = ingredient_lower.split()
                if not any(keyword in name_lower for keyword in keyword_parts):
                    continue 
            except:
                name = "N/A"

            try:
                price = product.find_element(By.CSS_SELECTOR, 'span.cXCGWM').text.strip().replace('$', '')
            except:
                price = "0.0"

            try: 
                 product.find_element(By.CSS_SELECTOR, '[data-testid="promo-label"]')
                 offer_discount.append("Yes")
            except: 
                 offer_discount.append("No")

            try: 
                 img_url = product.find_element(By.TAG_NAME, 'img').get_attribute('src')
                 if not img_url.startswith("https://media.nedigital.sg/"):
                    img_url = "https://cdn-icons-png.flaticon.com/512/135/135620.png"

            except: 
                 img_url="https://cdn-icons-png.flaticon.com/512/135/135620.png"

            try: 
                product_link = product.find_element(By.TAG_NAME, 'a').get_attribute('href')
                
            except: 
                product_link=""

            product_name.append(name)
            product_price.append(price)
            product_url.append(product_link)
            image_url.append(img_url)
            # product_url.append("N/A")
            in_stock.append("Yes")
            #offer_discount.append("No")  # Add promo parsing if needed

        df = pd.DataFrame({
            "Product Name": product_name,
            "Product Price": product_price,
            "in-stock": in_stock,
            "offer": offer_discount,
            "product url": product_url,
            "image url":image_url
        })

        # Filter based on min/max
        df["Product Price"] = pd.to_numeric(df["Product Price"], errors="coerce")

        print("MIN:", min_price)
        print("MAX:", max_price)
        print(df[["Product Name", "Product Price"]])

        df = df[df["Product Price"].between(float(min_price), float(max_price))]

        #Get top 5 products 
        df = df.head(5)

        return df.to_dict(orient="records")

    except Exception as e:
        return [{"error": str(e)}]

    finally:
        driver.quit()





