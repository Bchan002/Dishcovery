package finalProject.dishcoveryServer.utils;

public class userSql {

        public static final String INSERT_SIGN_UP_DETAILS = """
                        INSERT into userDetails (userId,email,username,password) values (?,?,?,?)
                        """;

        public static final String GET_USER_DETAILS = """
                        SELECT * FROM userDetails WHERE username = ? AND password = ?
                        """;

        public static final String GET_USERNAME = """
                        SELECT * FROM userDetails WHERE username = ?
                        """;

        public static final String CHECK_EMAIL_EXISTS = """
                        SELECT password, username FROM userDetails WHERE email= ?
                        """;

        public static final String UPDATE_PASSWORD = """
                       UPDATE userDetails 
                        SET 
                        password = ?
                        WHERE email = ?

                        """;

        public static final String GET_USER_ID = """
                                SELECT userId from userDetails WHERE username = ?
                                 """;

        public static final String GET_EMAIL = """
                        SELECT email from userDetails WHERE userId = ? 
                        """;
}
