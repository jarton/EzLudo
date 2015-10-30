package no.hig.ezludo;

import junit.framework.TestCase;
import org.junit.Test;

import java.sql.*;

/**
 * Created by jdr on 30/10/15.
 */
public class DbTest extends TestCase {
    private final static String dbUrl = "jdbc:derby:ezLudoServer;";
    private static Connection database;
    private final static String email = "test@test.no";
    private final static String password = "1234";
    private final static String nickname = "testolini";

    @Test
    public void testWrite() {
        try {
            database = DriverManager.getConnection(dbUrl);
            PreparedStatement query = database.prepareStatement("insert into users (email, password, nickname) " +
                    "VALUES (?, ?, ?)");
            query.setString(1, email);
            query.setString(2, password);
            query.setString(3, nickname);
            int result = query.executeUpdate();
            if (result < 1)
                fail();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    @Test
    public void testRead() {
        try {
            PreparedStatement query = database.prepareStatement("SELECT id, nickname FROM users WHERE email=? and password=?");
            query.setString(1, email);
            query.setString(2, password);
            ResultSet result = query.executeQuery();
            if (!result.next())
                fail();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
}
