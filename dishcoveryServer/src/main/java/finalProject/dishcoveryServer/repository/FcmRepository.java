package finalProject.dishcoveryServer.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import finalProject.dishcoveryServer.utils.fcmTokenSql;
import finalProject.dishcoveryServer.models.FCMToken;
import finalProject.dishcoveryServer.models.GroceryItem.GroceryItem;
import finalProject.dishcoveryServer.models.exception.GroceryNotSavedException;

@Repository
public class FcmRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean saveFcmToken(FCMToken fcmToken) {

        System.out.println("userId" + fcmToken.getUserId());
        System.out.println("fcmToken" + fcmToken.getFcmToken());

        // Check whether the token exists first

        try {

            // Check whether the token exists first
            Integer count = jdbcTemplate.queryForObject(
                fcmTokenSql.CHECK_FCM_TOKEN,
                new Object[]{fcmToken.getFcmToken()},
                Integer.class
            );

            if (count == 0) {
                int saved = jdbcTemplate.update(fcmTokenSql.INSERT_FCM, fcmToken.getUserId(), fcmToken.getFcmToken());

                if (saved > 0) {
                    return true;
                }

                return false;
            } else {

                // Update the userId
                int updated = jdbcTemplate.update(fcmTokenSql.UPDATE_FCM_TOKEN, fcmToken.getUserId(),
                        fcmToken.getFcmToken());

                if (updated > 0) {
                    return true;
                }

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new GroceryNotSavedException("Cannot save FCM token error");
        }
    }

    public Optional<List<FCMToken>> fetchSavedFcmToken(String userId) {

        List<FCMToken> fcmToken = new ArrayList<>();

        try {

            fcmToken = jdbcTemplate.query(fcmTokenSql.GET_SAVED_FCMToken,
                    BeanPropertyRowMapper.newInstance(FCMToken.class), userId);

            return Optional.of(fcmToken);

        } catch (Exception e) {

            throw new GroceryNotSavedException("Cannot fetch saved fcmToken");

        }

    }
}
