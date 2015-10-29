package no.hig.ezludo.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by jdr on 29/10/15.
 */
public class User {
    private Socket socket;
    private String nickName = "";
    private BufferedReader buffReader;
    private BufferedWriter buffWriter;
    private int uid = -1;

    public User (Socket socket) throws Exception {
        this.socket = socket;
        //TODO: verify key from user and
        // if key == 1234
        nickName = "test";
        uid = 1;
        // else socket.close;
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
