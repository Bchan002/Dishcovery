package finalProject.dishcoveryServer.models;

import java.util.List;

public class PriceCompare {
    
    private List<String> ingredients;
    private String minPrice;
    private String maxPrice;
    
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public String getMinPrice() {
        return minPrice;
    }
    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }
    public String getMaxPrice() {
        return maxPrice;
    }
    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }
    @Override
    public String toString() {
        return "PriceCompare [ingredients=" + ingredients + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice + "]";
    }

    
}
