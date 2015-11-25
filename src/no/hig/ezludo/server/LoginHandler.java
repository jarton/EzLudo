package no.hig.ezludo.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class handles login and registration of users. a object of this class
 * is created on each connection on the loginSocket of the server.
 * @author jdr
 */
public class LoginHandler {
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private Connection database;
    private static Logger logger = Logger.getAnonymousLogger();

    public LoginHandler (Socket socket, Connection db) throws Exception {
        database = db;
        try {
            buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String info[] = buffReader.readLine().split("\\|");

            if (info[0].equals("LOGIN")) {
                login(info);
            } else if (info[0].equals("REGISTER")) {
                register(info);
            } else {
                throw new Exception ("Unknown command from client");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }

    /**
     * checks the loginInformation from the user with that in the database.
     * if successful returns ok and also a key the user gives when connecting to
     * the main socket showing that they have already logged in.
     * @param info an array with the loginInformation
     */
    private void login(String info[]) {
        try {
            PreparedStatement query = database.prepareStatement("SELECT id, nickname FROM users WHERE email=? and password=?");
            query.setString(1, info[1]);
            query.setString(2, info[2]);
            ResultSet result = query.executeQuery();
            if (!result.next()) {
                writeToBuffer("Uknown username/password");
            } else {
                int uid = result.getInt(1);
                Date now = new Date();
                String nickname = result.getString(2);
                query.close();
                String keyString = nickname + now.toString();
                byte[] bytesOfMessage = keyString.getBytes("UTF-8");

                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] keyBytes = md.digest(bytesOfMessage);
                String key = new String(keyBytes);
                key = key.substring(0, key.length()/2);
                query = database.prepareStatement("UPDATE users SET loginkey=? WHERE id=?");
                query.setString(1, key);
                query.setInt(2, uid);
                query.execute();
                writeToBuffer("LOGIN OK|" + key);
            }
        } catch (SQLException sqlEx) {
            logger.log(Level.SEVERE, "an exception was thrown", sqlEx);
            writeToBuffer("Database error");
        } catch (UnsupportedEncodingException encEx) {
            logger.log(Level.SEVERE, "an exception was thrown", encEx);

        } catch (NoSuchAlgorithmException algEx) {
            logger.log(Level.SEVERE, "an exception was thrown", algEx);
        }

    }

    /**
     * puts the registration iformation in the database and lets user know if registratin was
     * successful or not.
     * @param info an array with registration information
     */
    private void register(String info[]) {
        try {
            PreparedStatement query = database.prepareStatement("insert into users (email, password, nickname) " +
                    "VALUES (?, ?, ?)");
            query.setString(1, info[1]); // email
            query.setString(2, info[2]); // password
            query.setString(3, info[3]); // nickname
            int result = query.executeUpdate();
            if (result < 1) {
                writeToBuffer("Username Occupied");
            }
            else {
                writeToBuffer("REGISTRATION OK");
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            logger.log(Level.SEVERE, "an exception was thrown", sqlEx);
        }
    }

    /**
     * writes a string in the socket buffer.
     * @param string the string to write to the client
     */
    private void writeToBuffer(String string) {
        try {
            buffWriter.write(string);
            buffWriter.newLine();
            buffWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "an exception was thrown", e);
        }
    }
}
