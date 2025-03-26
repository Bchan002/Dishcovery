package finalProject.dishcoveryServer.utils;

public class shoppingListSql {
    
    public final static String SAVE_SHOPPING_LIST = """
            INSERT into shoppingList (shoppingListId,userId) values (?,?)
            """;

    public final static String FETCH_SHOPPING_LIST = """
            SELECT shoppingListId FROM shoppingList WHERE userId = ?
            """;

    public final static String DELETE_SHOPPING_LIST = """
            DELETE FROM shoppingList WHERE shoppingListId = ?
            """;
}
