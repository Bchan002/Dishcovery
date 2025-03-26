# app.py

from flask import Flask, request, jsonify
from scraper.fairPriceScraper import scrape
from urllib.parse import unquote

app = Flask(__name__)

@app.route("/scrape", methods=["GET"])
def scrape_route():
    ingredient = request.args.get("ingredient")
    print("Received ingredient:", request.args.get("ingredient"))
    decoded_ingredient = unquote(ingredient)  # ✅ Decode %20 to space
    print("Received ingredient:", decoded_ingredient)
    min_price = request.args.get("minPrice", "0")
    max_price = request.args.get("maxPrice", "9999")
    
    result = scrape(decoded_ingredient, min_price, max_price)
    return jsonify(result)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5001)
