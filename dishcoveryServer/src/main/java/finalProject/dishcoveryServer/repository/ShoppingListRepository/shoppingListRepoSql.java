package finalProject.dishcoveryServer.repository.ShoppingListRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import finalProject.dishcoveryServer.models.exception.EmailNotFoundException;
import finalProject.dishcoveryServer.models.exception.ShoppingListException;
import finalProject.dishcoveryServer.utils.shoppingListSql;

@Repository
public class shoppingListRepoSql {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /*
     *  SAVE TO SHOPPING LIST
     */
    public void saveShoppingList(String shoppingListId, String userId){

        try {
            
            int saved = jdbcTemplate.update(shoppingListSql.SAVE_SHOPPING_LIST,shoppingListId,userId);


        } catch (Exception e) {
            
            e.printStackTrace();
            throw new ShoppingListException("Cannot save shoppingList to mySql");
            
        }
    }


    /*
     *  FETCH SHOPPING LIST IDs
     */
    public List<String> fetchShoppingList(String userId){
        
         List<String> shoppingList = new ArrayList<>();
        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(shoppingListSql.FETCH_SHOPPING_LIST, userId);

            while (sqlRowSet.next()) {
                shoppingList.add(sqlRowSet.getString("shoppingListId"));
            }

            return shoppingList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ShoppingListException("Cannot fetch the shoppingListId from sql");

        }
    }


    /*
     *  DELETE SHOPPING LIST 
     */
    public Boolean deleteShoppingList(String shoppingListId){

        try {
            int deleted = jdbcTemplate.update(shoppingListSql.DELETE_SHOPPING_LIST,
                    shoppingListId);

            if (deleted > 0) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ShoppingListException("Cannot delete shoppingList in Mongo");

        }

    }




    
}
