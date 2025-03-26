package finalProject.dishcoveryServer.repository.RecipeRepository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import static finalProject.dishcoveryServer.utils.Constants.*;

@Repository
public class recipeRepository {

    @Autowired
    private MongoTemplate template;

    /*
     * Get all the popular Recipe out from mongo
     */
    public List<Document> getPopularRecipe() {

        List<Document> results = template.findAll(Document.class, C_POPULAR_RECIPE);

        return results;

    }

    /*
     * Save User Recipe
     */
    public void saveUserRecipe(Document doc) {

        Criteria criteria = Criteria.where("_id").is(doc.get("recipeId"));

        Query query = Query.query(criteria);

        Update updateOps = new Update()
                .set("title", doc.get("title"))
                .set("readyInMinutes", doc.get("readyInMinutes"))
                .set("servings", doc.get("servings"))
                .set("image", doc.get("image"))
                .set("summary", doc.get("summary"))
                .set("instructions", doc.get("instructions"))
                .set("ingredients", doc.get("ingredients"));

        UpdateResult updateResult = template.upsert(query, updateOps, C_SAVED_USER_RECIPE);
    }

    /**
     * 
     * GET ALL USER SAVED RECIPES
     */
    public Document getSavedRecipes(String recipeId) {

        Criteria criteria = Criteria.where("_id").is(recipeId);

        Query query = Query.query(criteria);

        Document savedRecipe = template.findOne(query, Document.class, C_SAVED_USER_RECIPE);

        return savedRecipe;
    }

    /**
     * 
     * DELETE SAVED RECIPES
     */

    public void deleteSavedRecipes(String recipeId) {

        Criteria criteria = Criteria.where("_id").is(recipeId);

        Query query = Query.query(criteria)
                .limit(1);

        DeleteResult result = template.remove(query, C_SAVED_USER_RECIPE);

        // Get the document count
        int count = (int) result.getDeletedCount();


    }





    /*
     * Upsert (Combination of insert and update)
     * 
     * Preload the data inside
     */

    public void saveRecipe(List<Document> docs) {

        // template.insert(docs,"popularRecipe");

        for (Document doc : docs) {
            Criteria criteria = Criteria.where("_id").is(doc.get("recipeId"));

            Query query = Query.query(criteria);

            Update updateOps = new Update()
                    .set("title", doc.get("title"))
                    .set("category", doc.get("category"))
                    .set("readyInMinutes", doc.get("readyInMinutes"))
                    .set("servings", doc.get("servings"))
                    .set("image", doc.get("image"))
                    .set("summary", doc.get("summary"))
                    .set("instructions", doc.get("instructions"))
                    .set("ingredients", doc.get("ingredients"));

            UpdateResult updateResult = template.upsert(query, updateOps, C_POPULAR_RECIPE);

        }

    }
}
