package no.hig.ezludo.server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * this class represents a user on the server.
 * user informaton is contained here as well as a second login
 * that verifies with a key that the user has previously logged in,
 * if not the socket to the user is closed.
 * @author jdr
 */
public class User {
    private Socket socket;
    private String nickName = "";
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private int uid = -1;
    private Connection database;

    public User (Socket socket, Connection db) throws Exception {
        this.socket = socket;
        database = db;
		try {
			buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			String key = readLine();
			boolean login = userLogin(key);
            if (!login) {
                socket.close();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * verifyes a key agains the database to check if the user had previously logged in.
     * @param key the key from the user to verify
     * @return if the key was verifyed or not
     */
    private boolean userLogin(String key) {
        try {
            PreparedStatement stmnt = database.prepareStatement("SELECT id, nickname FROM users WHERE loginkey=?");
            stmnt.setString(1, key);
            ResultSet result = stmnt.executeQuery ();
            if (!result.next()) {
                write("LOGIN FAILED");
                return false;
            } else {
                uid = result.getInt(1);
                nickName = result.getString(2);
                stmnt.close();
                stmnt = database.prepareStatement("UPDATE users SET loginkey='' WHERE id=?");
                stmnt.setInt(1, uid);
                stmnt.execute();
                write("LOGGED IN|" + nickName);
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return true;
    }

    /**
     * writes a string back to the user through the socketBuffer.
     * @param string the string to write
     */
    public void write(String string) throws IOException {
            buffWriter.write(string);
            buffWriter.newLine();
            buffWriter.flush();
    }

    public void closeSocket() {
        try {
            socket.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * indicates if the user has written something in the socketBuffer
     * @return true or false if the user has sendt something
     * @throws IOException
     */
    public boolean ready () throws IOException {
        return buffReader.ready ();
    }

    /**
     * reads a line in the buffer from the user
     * @return the message from the user
     * @throws IOException to user can be removed
     */
    public String readLine () throws IOException {
        return buffReader.readLine();
    }

    /**
     * returns this users nickName
     * @return the nickname of the userObject
     */
    public String getNickname () {
        return nickName;
    }

}
