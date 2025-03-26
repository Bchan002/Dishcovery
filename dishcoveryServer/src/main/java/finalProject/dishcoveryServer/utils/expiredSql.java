package finalProject.dishcoveryServer.utils;

public class expiredSql {
    
    public static final String SAVE_EXPIRED_ITEMS = """
            INSERT into expired_items (userId,expiredItem,expirationDate) values (?,?,?)
            """;

    public static final String GET_EXPIRED_ITEMS = """
            SELECT * FROM expired_items WHERE userId = ?
            """;

    public static final String CLEAR_EXPIRED_ITEMS = """
            DELETE FROM expired_items WHERE userId= ?
            """;
}
