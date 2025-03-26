package finalProject.dishcoveryServer.models;

import java.util.List;

public class ShoppingList {

    private String recipeName;
    private String recipeImage;
    private List<String> ingredients;

    
    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public String getRecipeImage() {
        return recipeImage;
    }
    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    

}
