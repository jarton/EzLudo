package no.hig.ezludo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jdr on 29/10/15.
 */
public class CreateDb {

        private final static String dbUrl = "jdbc:derby:ezLudoServer;create=true";
    private static Logger logger = Logger.getAnonymousLogger();

        private CreateDb () {
            setup();
        }

        public static void setup () {
            try {
                Connection con =  DriverManager.getConnection(dbUrl);
                Statement stmt = con.createStatement();
                stmt.execute("CREATE TABLE users (id bigint NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "+
                        "nickname varchar(32) NOT NULL,"+
                        "email varchar(128) NOT NULL, "+
                        "password char(128) NOT NULL, "+
                        "loginkey char(128), "+
                        "PRIMARY KEY  (id))");
                //"UNIQUE (email))");
                con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "an exception was thrown", e);
            }
        }

    public static void main(String[] args) {
            CreateDb createDb;
            createDb = new CreateDb();
        }
}
