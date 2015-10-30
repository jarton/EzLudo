package no.hig.ezludo.server;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jdr on 29/10/15.
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

    private boolean userLogin(String key) {
        try {
            PreparedStatement stmnt = database.prepareStatement("SELECT id, nickname FROM users WHERE loginkey=?");
            stmnt.setString(1, key);
            ResultSet result = stmnt.executeQuery ();
            if (!result.next()) {
                write("Uknown username/password");
                return false;
            } else {
                uid = result.getInt(1);
                nickName = result.getString(2);
                stmnt.close();
                stmnt = database.prepareStatement("UPDATE users SET loginkey='' WHERE id=?");
                stmnt.setInt(1, uid);
                stmnt.execute();
                write("Logged In\t" + key);
            }
        } catch (SQLException sqlEx) {
            write("Database error");
        }
        return true;
    }

    public void write(String string) {
        try {
            buffWriter.write(string);
            buffWriter.newLine();
            buffWriter.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean ready () throws IOException {
        return buffReader.ready ();
    }

    public String readLine () throws IOException {
        return buffReader.readLine();
    }

    public String getNickname () {
        return nickName;
    }

}
