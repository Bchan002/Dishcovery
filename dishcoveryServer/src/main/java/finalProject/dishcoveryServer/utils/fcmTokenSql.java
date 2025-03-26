package finalProject.dishcoveryServer.utils;

public class fcmTokenSql {
    
    public static final String INSERT_FCM= """
        INSERT into userFCMTokens (userId,fcmToken) values (?,?)
        """;

    public static final String GET_SAVED_FCMToken = """
            SELECT * FROM userFCMTokens WHERE  userId = ?;
            """;

    public static final String CHECK_FCM_TOKEN = """
            SELECT COUNT(*) FROM userFCMTokens WHERE fcmToken = ?
            """;

    public static final String UPDATE_FCM_TOKEN = """
            UPDATE userFCMTokens SET userId = ? WHERE fcmToken=?
            """;
}
