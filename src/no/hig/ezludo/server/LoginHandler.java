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

/**
 * Created by jdr on 29/10/15.
 */
public class LoginHandler {
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private Connection database;

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
            e.printStackTrace();
        }
    }

    private void login(String info[]) {
        try {
            PreparedStatement query = database.prepareStatement("SELECT id, nickname FROM users WHERE email=? and password=?");
            query.setString(1, info[1]);
            query.setString(2, info[2]);
            ResultSet result = query.executeQuery ();
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
                byte[] key = md.digest(bytesOfMessage);
                query = database.prepareStatement("UPDATE users SET loginkey=? WHERE id=?");
                query.setString(1, new String(key));
                query.setInt(2, uid);
                query.execute();
                writeToBuffer("LOGIN OK|" + key);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            writeToBuffer("Database error");
        } catch (UnsupportedEncodingException encEx) {
            encEx.printStackTrace();

        } catch (NoSuchAlgorithmException algEx) {
            algEx.printStackTrace();
        }

    }

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
            else
                writeToBuffer("REGISTRATION OK");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    private void writeToBuffer(String string) {
        try {
            buffWriter.write(string);
            buffWriter.newLine();
            buffWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
