package finalProject.dishcoveryServer.service.AskAIService;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import finalProject.dishcoveryServer.models.exception.RecipeNotFoundException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;

import java.io.*;

@Service
public class askAIService {

    @Value("${openai.api.key}")
    private String API_KEY;

    private String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private String OPENAI_IMAGES_URL = "https://api.openai.com/v1/images/generations";

    public JsonArray getSuggestedRecipes(List<String> ingredients) {

        // Build a prompt
        String prompt = createPrompt(ingredients);

        // Create the body
        JsonArrayBuilder messages = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("role", "user")
                        .add("content", prompt));

        JsonObject body = Json.createObjectBuilder()
                .add("model", "gpt-3.5-turbo")
                .add("messages", messages)
                .add("temperature", 0.7)
                .build();

        // Step 2: Create the RequestEntity
        RequestEntity<String> req = RequestEntity
                .post(OPENAI_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY) //
                .body(body.toString());

        // Step 2: Create a RestTemplate
        RestTemplate template = new RestTemplate();

        // Step 3: Configure the response
        ResponseEntity<String> resp;

        try {

            // Step 4: Send the request and get the resposne as a String
            resp = template.exchange(req, String.class);

            /// If required can check for status code 
            // Check for errors
            if (resp.getStatusCode().is4xxClientError()) {
                throw new Exception("Authentication failed: Invalid username or password");
            }

            // Step 5: Extract the payload (JSON String format)
            String payload = resp.getBody();

            // Step 6: Read the JSON format payload
            Reader reader = new StringReader(payload);
            JsonReader jsonReader = Json.createReader(reader);

            JsonObject obj = jsonReader.readObject();

            String content = obj.getJsonArray("choices")
                    .getJsonObject(0)
                    .getJsonObject("message")
                    .getString("content");

            // Convert
            Reader reader2 = new StringReader(content);
            JsonReader jsonReader2 = Json.createReader(reader2);

            JsonArray contentArray = jsonReader2.readArray();

            // session.setAttribute("contentArray", contentArray.toString());
            List<String> imagesUrl = new ArrayList<>();
            // Get the title name and create a list of images url
            for (int i = 0; i < contentArray.size(); i++) {
                JsonObject recipeObj = contentArray.getJsonObject(i);
                String recipeTitle = recipeObj.getString("title");

                String recipeImages = generateRecipeImages(recipeTitle);

                imagesUrl.add(recipeImages);

                Thread.sleep(1500);

            }

            JsonArray finalJson = convertToFinal(contentArray, imagesUrl);

            return finalJson;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RecipeNotFoundException("No recipe found");

        }

    }

    /*
     * GENERATE FINAL JSON ARRAY
     */
    // public JsonArray convertToFinal(JsonArray array, List<String> imagesUrl) {

    //     JsonArrayBuilder finalArrayBuilder = Json.createArrayBuilder();

    //     for (int i = 0; i < array.size(); i++) {
    //         JsonObject recipe = array.getJsonObject(i);
    //         String imageUrl = (i < imagesUrl.size()) ? imagesUrl.get(i) : "";

    //         // Build a new object with all existing fields + image
    //         JsonObjectBuilder updatedRecipe = Json.createObjectBuilder(recipe);
    //         updatedRecipe.add("image", imageUrl);

    //         finalArrayBuilder.add(updatedRecipe);
    //     }

    //     return finalArrayBuilder.build();
    // }

    public JsonArray convertToFinal(JsonArray array, List<String> imagesUrl) {

        JsonArrayBuilder finalArrayBuilder = Json.createArrayBuilder();
    
        for (int i = 0; i < array.size(); i++) {
            JsonObject recipe = array.getJsonObject(i);
            String imageUrl = (i < imagesUrl.size()) ? imagesUrl.get(i) : "";
    
            // 🔁 Create new ingredients array with fixed amounts
            JsonArray ingredients = recipe.getJsonArray("ingredients");
            JsonArrayBuilder newIngredients = Json.createArrayBuilder();
    
            for (JsonObject ing : ingredients.getValuesAs(JsonObject.class)) {
                String originalAmount = ing.get("amount").toString().replace("\"", "");
                String cleanedAmount = convertFractionToDecimal(originalAmount);
    
                newIngredients.add(Json.createObjectBuilder()
                    .add("name", ing.getString("name"))
                    .add("amount", cleanedAmount)
                    .add("unit", ing.getString("unit"))
                );
            }
    
            // 🔧 Build updated recipe object with fixed ingredients and added image
            JsonObjectBuilder updatedRecipe = Json.createObjectBuilder();
    
            for (String key : recipe.keySet()) {
                if (key.equals("ingredients")) {
                    updatedRecipe.add("ingredients", newIngredients);
                } else {
                    updatedRecipe.add(key, recipe.get(key));
                }
            }
    
            updatedRecipe.add("image", imageUrl); // Add image
    
            finalArrayBuilder.add(updatedRecipe);
        }
    
        return finalArrayBuilder.build();
    }
    
    private String convertFractionToDecimal(String value) {
        switch (value.trim()) {
            case "1/4": return "0.25";
            case "1/2": return "0.5";
            case "3/4": return "0.75";
            case "1/3": return "0.33";
            case "2/3": return "0.67";
            case "1/8": return "0.125";
            case "1":   return "1.0";
            default: return value; // Return as-is if it's already decimal
        }
    }
    

    private String createPrompt(List<String> ingredients) {
        return "Based on the following ingredients: " + String.join(", ", ingredients) +
        ", give me 5 unique recipes in JSON format like this:\n\n" +
        """
        [
          {
            "recipeId": 12345678,
            "title": "Sample Dish",
            "category": "Lunch",
            "readyInMinutes": 30,
            "servings": 2,
            "summary": "A short summary here.",
            "instructions": "Step by step instructions.",
            "ingredients": [
              {"name": "chicken", "amount": "200", "unit": "g"},
              {"name": "salt", "amount": "1", "unit": "tsp"}
            ]
          }
        ]
        STRICT RULES:
          - 'recipeId': 8-digit random number
          - 'servings': must be an integer (e.g., 2, 4, 6)
          - 'amount': MUST BE a decimal number (e.g., 0.5, 1.0, 2.25). DO NOT use fractions like '1/2', '¼', '¾', or similar — not even inside strings.
          - 'instructions': must be a single plain English string. No HTML or line breaks.
          - No nulls, placeholders, or missing fields.
          - Output MUST strictly match the JSON structure above.
          - If any rule is violated, regenerate the recipe internally and retry until it passes all rules.
        """;
    }

    /*
     * OPEN AI TO GENERATE IMAGES
     */
    public String generateRecipeImages(String recipeTitle) {

        String prompt = "A high quality food photo of " + recipeTitle + ", plated nicely and top down view";

        JsonObject body = Json.createObjectBuilder()
                .add("prompt", prompt)
                .add("n", 1)
                .add("size", "512x512")
                .build();

        // Step 2: Create the RequestEntity
        RequestEntity<String> req = RequestEntity
                .post(OPENAI_IMAGES_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY) //
                .body(body.toString());

        RestTemplate template = new RestTemplate();

        // Step 3: Configure the response
        ResponseEntity<String> resp;

        try {

            // Step 4: Send the request and get the resposne as a String
            resp = template.exchange(req, String.class);

            /// If required can check for status code 
            // Check for errors
            if (resp.getStatusCode().is4xxClientError()) {
                throw new Exception("Error");
            }

            // Step 5: Extract the payload (JSON String format)
            String payload = resp.getBody();
            Reader reader = new StringReader(payload);
            JsonReader jsonReader = Json.createReader(reader);

            JsonObject obj = jsonReader.readObject();

            String imagesUrl = obj
                    .getJsonArray("data")
                    .getJsonObject(0)
                    .getString("url");

            return imagesUrl;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RecipeNotFoundException("No recipe found");

        }
    }

}
