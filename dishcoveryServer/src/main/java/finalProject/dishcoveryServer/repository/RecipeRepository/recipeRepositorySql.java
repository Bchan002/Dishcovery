package finalProject.dishcoveryServer.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import finalProject.dishcoveryServer.utils.recipeSql;
import finalProject.dishcoveryServer.models.exception.EmailNotFoundException;
import finalProject.dishcoveryServer.models.exception.SignUpUnsuccessfulExcpetion;

@Repository
public class recipeRepositorySql {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * Get the User Id from the table
     */
    public Boolean saveRecipeId(String userId, String recipeId) {

        try {
            int inserted = jdbcTemplate.update(recipeSql.INSERT_RECIPE_ID,
                    recipeId, userId);

            if (inserted > 0) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    /*
     * Get All RecipeId from userRecipeDetail
     * 
     */
    public List<String> allRecipeId(String userId) {

        List<String> recipeIdList = new ArrayList<>();
        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(recipeSql.GET_ALL_RECIPE_ID, userId);

            while (sqlRowSet.next()) {
                recipeIdList.add(sqlRowSet.getString("recipeId"));
            }

            return recipeIdList;

        } catch (Exception e) {
            throw new EmailNotFoundException("Recipe not found");

        }

    }

    /*
     *  DELETE RecipeID from userRecipes table
     */
    public Boolean deleteSavedRecipes(String recipeId){

        try {
            int deleted = jdbcTemplate.update(recipeSql.DELETE_RECIPE_ID,
                    recipeId);

            if (deleted > 0) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

}
