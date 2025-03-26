package finalProject.dishcoveryServer.utils;

public class recipeSql {

    public static final String INSERT_RECIPE_ID= """
                        INSERT into userRecipes (recipeId,userId) values (?,?)
                        """;

    public static final String GET_ALL_RECIPE_ID= """
                            SELECT recipeId from userRecipes WHERE userId = ?
                            """;

    public static final String DELETE_RECIPE_ID = """
             DELETE FROM userRecipes WHERE recipeId = ?
            """;
    
}
