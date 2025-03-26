package finalProject.dishcoveryServer.service.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.io.*;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import finalProject.dishcoveryServer.models.Recipe.Ingredient;
import finalProject.dishcoveryServer.models.Recipe.Recipe;
import finalProject.dishcoveryServer.models.exception.RecipeNotFoundException;
import finalProject.dishcoveryServer.repository.userRepository;
import finalProject.dishcoveryServer.repository.RecipeRepository.recipeRepository;
import finalProject.dishcoveryServer.repository.RecipeRepository.recipeRepositorySql;
import finalProject.dishcoveryServer.utils.recipeSql;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Service
public class RecipeService {

    @Autowired
    private recipeRepository recipeRepo;

    @Autowired
    private userRepository userRepo;

    @Autowired
    private recipeRepositorySql recipeRepoSql;

    @Value("${api.key}")
    private String API_KEY;

    private String GET_URL = "https://api.spoonacular.com/recipes/random";
    private String COMPLEX_SEARCH_URL = "https://api.spoonacular.com/recipes/complexSearch";

    /*
     * GET POPULAR RECIPE
     * 
     */
    public List<JsonObject> getPopularRecipe() {

        List<Document> popularRecipeDocs = recipeRepo.getPopularRecipe();

        // Convert to JsonObject
        List<JsonObject> popularRecipeJson = convertJsonToJsonObjectList(popularRecipeDocs);

        return popularRecipeJson;

    }

    public List<JsonObject> convertJsonToJsonObjectList(List<Document> docs) {
        // List<JsonObject> jsonObjects = new ArrayList<>();

        List<JsonObject> recipeJson = docs.stream()
                .map(a -> {

                    // Get the recipeID
                    Integer recipeId = (Integer) a.get("_id");
                    JsonObjectBuilder recipeBuilder = Json.createObjectBuilder()
                            .add("recipeId", recipeId)
                            .add("title", a.getString("title"))
                            .add("readyInMinutes", a.getInteger("readyInMinutes"))
                            .add("servings", a.getInteger("servings"))
                            .add("category", a.getString("category"))
                            .add("image",
                                    a.containsKey("image") ? a.getString("image")
                                            : "https://www.foodiesfeed.com/wp-content/uploads/2023/09/healthy-food.jpg")
                            .add("summary", a.containsKey("summary") ? a.getString("summary") : "")
                            .add("instructions", a.containsKey("instructions") ? a.getString("instructions") : "");

                    if (a.containsKey("ingredients")) {
                        // Get the ingredients as a List<Document>
                        List<Document> ingredientsArray = (List<Document>) a.getList("ingredients", Document.class);

                        // Create a JsonArrayBuilder to hold all the ingredients
                        JsonArrayBuilder ingredientsBuilder = Json.createArrayBuilder();

                        // Convert each ingredient into a JsonObject
                        ingredientsArray.forEach(b -> {
                            JsonObject ingredientObj = Json.createObjectBuilder()
                                    .add("name", b.getString("name"))
                                    .add("amount", b.getDouble("amount"))
                                    .add("unit", b.containsKey("unit") ? b.getString("unit") : "")
                                    .build();

                            // Add the ingredient JsonObject to the ingredients array
                            ingredientsBuilder.add(ingredientObj);
                        });

                        // Add the ingredients to the recipe
                        recipeBuilder.add("ingredients", ingredientsBuilder.build());
                    }

                    // Return a Json Object
                    return recipeBuilder.build();
                })
                .collect(Collectors.toList());

        return recipeJson;
    }

    /*
     * User Search Recipe
     */
    public List<JsonObject> searchRecipe(String query) {

        String url = UriComponentsBuilder
                .fromUriString(COMPLEX_SEARCH_URL) // base url
                .queryParam("number", "10")
                .queryParam("query", query)
                .queryParam("sort", "rating")
                .queryParam("apiKey", API_KEY)
                .toUriString();

        // Step 1: Configure the request
        RequestEntity<Void> req = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

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

            // Step 7: check whether is it an JSON object or JSON array
            JsonObject jsonObject = jsonReader.readObject();

            // Create an List to put the id inside thn we search again...
            JsonArray jsonArray = jsonObject.getJsonArray("results");
            List<Integer> getRecipeId = new ArrayList<>();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject recipe = jsonArray.getJsonObject(i);

                Integer recipeId = recipe.getInt("id");
                getRecipeId.add(recipeId);

            }

            List<JsonObject> searchedRecipes = getSearchedRecipeDetails(getRecipeId);

            if (searchedRecipes == null || searchedRecipes.size() == 0) {
                throw new RecipeNotFoundException("No recipe found");
            }

            return searchedRecipes;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RecipeNotFoundException("No recipe found");

        }

    }

    public List<JsonObject> getSearchedRecipeDetails(List<Integer> recipeIds) {
        List<JsonObject> recipeDetailsList = new ArrayList<>();

        for (int id : recipeIds) {
            try {
                // Call API to get details (Replace this with actual API call)
                String apiUrl = "https://api.spoonacular.com/recipes/" + id + "/information";
                JsonObject recipe = fetchRecipeDetails(apiUrl); // Assume fetchRecipeDetails() gets the JSON

                // Extract only needed details
                JsonObjectBuilder recipeInfo = Json.createObjectBuilder()
                        .add("recipeId", id)
                        .add("title", recipe.getString("title"))
                        .add("image", recipe.getString("image"))
                        .add("readyInMinutes", recipe.getInt("readyInMinutes"))
                        .add("servings", recipe.getInt("servings"))
                        .add("sourceUrl", recipe.getString("sourceUrl"))
                        .add("vegetarian", recipe.getBoolean("vegetarian"))
                        .add("vegan", recipe.getBoolean("vegan"))
                        .add("glutenFree", recipe.getBoolean("glutenFree"))
                        .add("dairyFree", recipe.getBoolean("dairyFree"))
                        .add("spoonacularScore", recipe.getJsonNumber("spoonacularScore").doubleValue());

                // ✅ **Fix: Extract full ingredient details (name, amount, unit)**
                JsonArray ingredientsArray = recipe.getJsonArray("extendedIngredients");
                JsonArrayBuilder ingredientDetails = Json.createArrayBuilder();

                for (JsonValue element : ingredientsArray) {
                    JsonObject ingredient = element.asJsonObject();

                    String ingredientName = ingredient.getString("name", "Unknown Ingredient");
                    double amount = ingredient.containsKey("amount") ? ingredient.getJsonNumber("amount").doubleValue()
                            : 0.0;
                    String unit = ingredient.getString("unit", "");

                    JsonObject ingredientInfo = Json.createObjectBuilder()
                            .add("name", ingredientName)
                            .add("amount", amount)
                            .add("unit", unit)
                            .build();

                    ingredientDetails.add(ingredientInfo);
                }

                recipeInfo.add("ingredients", ingredientDetails);

                // Get dish types
                recipeInfo.add("dishTypes", recipe.getJsonArray("dishTypes"));

                // Get instructions
                recipeInfo.add("instructions", recipe.getString("instructions"));

                // Add to final list
                recipeDetailsList.add(recipeInfo.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return recipeDetailsList;
    }

    private JsonObject fetchRecipeDetails(String apiUrl) {

        String url = UriComponentsBuilder
                .fromUriString(apiUrl) // base url
                .queryParam("apiKey", API_KEY)
                .toUriString();

        // Step 1: Configure the request
        RequestEntity<Void> req = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

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

            // Step 7: check whether is it an JSON object or JSON array
            JsonObject jsonObject = jsonReader.readObject();

            return jsonObject;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RecipeNotFoundException("No recipe found");

        }

    }

    /*
     * SAVE RECIPE
     */
    public void saveRecipe(Recipe recipe, String username) {

        // Generate UUID
        String recipeId = UUID.randomUUID().toString().substring(0, 8);

        // Get the userId from userDetails
        String userId = userRepo.getUserId(username);

        // Send to mysql the recipeID and userId
        recipeRepoSql.saveRecipeId(userId, recipeId);

        // Convert the recipe into Document
        Document recipeDoc = convertRecipeDetailToDocument(recipe, recipeId);

        // Add to mongoDB 
        recipeRepo.saveUserRecipe(recipeDoc);

    }

    public Document convertRecipeDetailToDocument(Recipe recipe, String recipeId) {
        
        Document recipeDoc = new Document()
            .append("recipeId", recipeId)  // Use generated UUID
            .append("title", recipe.getTitle())
            .append("readyInMinutes", recipe.getReadyInMinutes())
            .append("servings", recipe.getServings())
            .append("image", recipe.getImage() != null ? recipe.getImage() : "")
            .append("ingredients", convertIngredientsToDocuments(recipe.getIngredients()))
            .append("summary",recipe.getSummary()!=null ? recipe.getSummary(): "")
            .append("instructions",recipe.getInstructions());

        return recipeDoc;
    
    }


    public List<Document> convertIngredientsToDocuments(List<Ingredient> ingredients){

        List<Document> ingredientDocs = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            Document ingredientDoc = new Document()
                    .append("name", ingredient.getName())
                    .append("amount", ingredient.getAmount())
                    .append("unit", ingredient.getUnit());
            ingredientDocs.add(ingredientDoc);
        }
        return ingredientDocs;
    }
    

    /*
     *  GET SAVED RECIPE
     */

    public List<JsonObject> getAllSavedRecipes(String username){

        List<Document> savedRecipeDocs = new ArrayList<>();
        // Get the userId from the userRepo
        String userId = userRepo.getUserId(username);
        // Get the recipeId from recipeRepoSql
        List<String> recipeIdList = recipeRepoSql.allRecipeId(userId);

        for(String recipeId: recipeIdList){

            // For each recipeId -> get The document out from recipe repo 
            Document recipeDoc = recipeRepo.getSavedRecipes(recipeId);

            // Add to the savedRecipeDocs 
            savedRecipeDocs.add(recipeDoc);

        }

        // Transform the savedRecipeDocs into List<JsonObject>
        List<JsonObject> savedRecipeList = this.convertDocumentToJsonObject(savedRecipeDocs);

        return savedRecipeList;

    }


    // public List<JsonObject> convertDocumentToJsonObject(List<Document> docs) {
    //     return docs.stream()
    //             .map(doc -> {
    //                 // Create JSON Object Builder
    //                 JsonObjectBuilder recipeBuilder = Json.createObjectBuilder()
    //                         .add("recipeId", doc.getString("_id")) // MongoDB _id is a String (UUID)
    //                         .add("title", doc.getString("title"))
    //                         .add("readyInMinutes", doc.getInteger("readyInMinutes"))
    //                         .add("servings", doc.getInteger("servings"))
    //                         .add("image", doc.containsKey("image") ? doc.getString("image") : "https://www.foodiesfeed.com/wp-content/uploads/2023/09/healthy-food.jpg")
    //                         .add("summary", doc.containsKey("summary") ? doc.getString("summary") : "")
    //                         .add("instructions", doc.containsKey("instructions") ? doc.getString("instructions") : "");
    
    //                 // Check if "ingredients" exists in the document
    //                 if (doc.containsKey("ingredients")) {
    //                     List<Document> ingredientsList = doc.getList("ingredients", Document.class);
    //                     JsonArrayBuilder ingredientsBuilder = Json.createArrayBuilder();
    
    //                     for (Document ingredient : ingredientsList) {
    //                         JsonObject ingredientObj = Json.createObjectBuilder()
    //                                 .add("name", ingredient.getString("name"))
    //                                 .add("amount", ingredient.get("amount") instanceof Integer
    //                                         ? ingredient.getInteger("amount")
    //                                         : ingredient.getDouble("amount"))
    //                                 .add("unit", ingredient.containsKey("unit") ? ingredient.getString("unit") : "")
    //                                 .build();
    //                         ingredientsBuilder.add(ingredientObj);
    //                     }
    //                     recipeBuilder.add("ingredients", ingredientsBuilder.build());
    //                 }
    
    //                 return recipeBuilder.build();
    //             })
    //             .collect(Collectors.toList());
    // }


    public List<JsonObject> convertDocumentToJsonObject(List<Document> docs) {
        return docs.stream()
                .map(doc -> {
                    JsonObjectBuilder recipeBuilder = Json.createObjectBuilder()
                            .add("recipeId", safeString(doc.getString("_id")))
                            .add("title", safeString(doc.getString("title")))
                            .add("readyInMinutes", toInt(doc.get("readyInMinutes")))
                            .add("servings", toInt(doc.get("servings")))
                            .add("image", safeString(doc.getString("image"), "https://www.foodiesfeed.com/wp-content/uploads/2023/09/healthy-food.jpg"))
                            .add("summary", safeString(doc.getString("summary")))
                            .add("instructions", safeString(doc.getString("instructions")));
    
                    if (doc.containsKey("ingredients")) {
                        List<Document> ingredientsList = doc.getList("ingredients", Document.class);
                        JsonArrayBuilder ingredientsBuilder = Json.createArrayBuilder();
    
                        for (Document ingredient : ingredientsList) {
                            JsonObjectBuilder ingBuilder = Json.createObjectBuilder();
    
                            // Handle 'name'
                            ingBuilder.add("name", safeString(ingredient.getString("name")));
    
                            // Handle 'amount'
                            Object amt = ingredient.get("amount");
                            if (amt instanceof Integer) {
                                ingBuilder.add("amount", (Integer) amt);
                            } else if (amt instanceof Double) {
                                ingBuilder.add("amount", (Double) amt);
                            } else if (amt instanceof Long) {
                                ingBuilder.add("amount", ((Long) amt).intValue());
                            } else {
                                ingBuilder.add("amount", 0); // fallback
                            }
    
                            // Handle 'unit'
                            ingBuilder.add("unit", safeString(ingredient.getString("unit")));
    
                            ingredientsBuilder.add(ingBuilder.build());
                        }
    
                        recipeBuilder.add("ingredients", ingredientsBuilder.build());
                    }
    
                    return recipeBuilder.build();
                })
                .collect(Collectors.toList());
    }
    
    private int toInt(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Double) return ((Double) value).intValue();
        if (value instanceof Long) return ((Long) value).intValue();
        return 0;
    }
    
    private String safeString(String value) {
        return value != null ? value : "";
    }
    
    private String safeString(String value, String fallback) {
        return value != null ? value : fallback;
    }
    








    /*
     *  DELETE SAVED RECIPE
     */
    public void deleteSavedRecipes(String recipeId){

        try {
            
            //Delete from mysql
            recipeRepoSql.deleteSavedRecipes(recipeId);

            //Delete from mongo 
            recipeRepo.deleteSavedRecipes(recipeId);

        } catch (Exception e) {
            // TODO: handle exception
            throw new RecipeNotFoundException("Save unsuccessful");
        }
     

    }













    /**
     * 
     * PRE LOAD THE DATA AND STORE IN MONGODB
     */

    public void storePopularRecipe(String tags) {

        String url = UriComponentsBuilder
                .fromUriString(GET_URL) // base url
                .queryParam("number", "20")
                .queryParam("tags", tags)
                .queryParam("apiKey", API_KEY)
                .toUriString();

        // Step 1: Configure the request
        RequestEntity<Void> req = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();

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

            // Step 7: check whether is it an JSON object or JSON array
            JsonObject jsonObject = jsonReader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("recipes");

            List<Document> docs = convertJsonToDocumentList(jsonArray, tags);

            // Send to repository
            recipeRepo.saveRecipe(docs);

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public List<Document> convertJsonToDocumentList(JsonArray jsonArray, String category) {

        List<Document> docs = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {

            JsonObject recipeJson = jsonArray.getJsonObject(i);

            Document recipeDoc = new Document()
                    .append("recipeId", recipeJson.getInt("id"))
                    .append("title", recipeJson.getString("title"))
                    .append("category", category)
                    .append("readyInMinutes", recipeJson.getInt("readyInMinutes"))
                    .append("servings", recipeJson.getInt("servings"))
                    .append("image", recipeJson.getString("image", ""))
                    .append("summary", recipeJson.getString("summary", ""))
                    .append("instructions", recipeJson.getString("instructions", ""));

            List<Document> ingredients = new ArrayList<>();
            JsonArray ingredientsArray = recipeJson.getJsonArray("extendedIngredients");

            for (int j = 0; j < ingredientsArray.size(); j++) {
                JsonObject ingredientJson = ingredientsArray.getJsonObject(j);
                Document ingredientDoc = new Document()
                        .append("name", ingredientJson.getString("name"))
                        .append("amount", ingredientJson.getJsonNumber("amount").doubleValue())
                        .append("unit", ingredientJson.getString("unit", ""));

                ingredients.add(ingredientDoc);

            }

            recipeDoc.put("ingredients", ingredients);

            docs.add(recipeDoc);

        }

        return docs;
    }

}
