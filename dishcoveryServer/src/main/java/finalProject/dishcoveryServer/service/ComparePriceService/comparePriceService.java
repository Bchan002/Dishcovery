package finalProject.dishcoveryServer.service.ComparePriceService;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import finalProject.dishcoveryServer.models.PriceCompare;
import finalProject.dishcoveryServer.models.exception.RecipeNotFoundException;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import java.io.*;

@Service
public class comparePriceService {
    
    private String BASE_URL = "https://vibrant-cooperation-production.up.railway.app/scrape";

    //Development 
    //private String BASE_URL = "http://localhost:5001/scrape";

    public JsonObject priceChecker(PriceCompare comparePrice){
        

        //Check ingredients -> for each ingredient get the array from webscraper 
        List<String> ingredients = comparePrice.getIngredients();
        JsonObjectBuilder results = Json.createObjectBuilder();

        ingredients.stream()
            .forEach(ingredient->{
                ingredient = ingredient.replaceAll("(?i)\\b(coarsely|finely|fresh|organic)\\b", "").trim();
                System.out.println("Your ingredient is " + ingredient);
                
                String min = comparePrice.getMinPrice().replace("$", "");
                String max = comparePrice.getMaxPrice().replace("$", "");

                //JsonArray eachResult = comparePrice(ingredient, comparePrice.getMinPrice(), comparePrice.getMaxPrice());
                JsonArray eachResult = getEachIngredient(ingredient, min,max);
                results.add(ingredient,eachResult);

            });

        JsonObject finalResult = results.build();

        return finalResult;

    }



    /*
     *  PRODUCTION 
     *   - CALLING THE FAIRPRICE SCRAPER API 
     */
    public JsonArray getEachIngredient(String ingredient,String minPrice, String maxPrice){

         String url = UriComponentsBuilder
                .fromUriString(BASE_URL) // base url
                .queryParam("ingredient",ingredient)
                .queryParam("minPrice",minPrice)
                .queryParam("maxPrice",maxPrice)
                .encode()
                .toUriString();

        System.out.println("Your url is " + url);

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
            JsonArray jsonArray= jsonReader.readArray();

            return jsonArray;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RecipeNotFoundException("No recipe found");

        }

        
    }




    /*
     *  DEVELOPMENT!!!!!
     * 
     *  BUILDING THE COMMAND USING ProcessBuilder
     * 
     *  
     */
    public JsonArray comparePrice(String ingredient, String min, String max){

        try {
            
            // InputStream in = getClass().getClassLoader().getResourceAsStream("scripts/Fairprice_Scraper.py");
            // File tempScript = File.createTempFile("Fairprice_Scraper", ".py");
            // Files.copy(in, tempScript.toPath(), StandardCopyOption.REPLACE_EXISTING);

             
            List<String> command = new ArrayList<>();
            command.add("python3");
            command.add("src/main/resources/scripts/Fairprice_Scraper.py");
            command.add(ingredient);
            command.add(min);
            command.add(max);

            // Procees builder -> run the script
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true); 
            Process process = processBuilder.start();

            // Read output 
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitcode = process.waitFor();
            System.out.println("Python script exited with code: " + exitcode);
            System.out.println("Scrape output:\n" + output);

            //Read the string 
            StringReader reader2 = new StringReader(output.toString());
            JsonReader jsonReader = Json.createReader(reader2);
            
            JsonArray array = jsonReader.readArray();

            return array;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
        
    }
}
