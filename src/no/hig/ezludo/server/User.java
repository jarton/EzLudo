package no.hig.ezludo.server;

import java.io.*;
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
        try {
            buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String args[] = buffReader.readLine().split("[\t]");
            if (args[0].equals("LOGIN")) {
                login (args, this.socket.getInetAddress().getHostAddress());
            } else if (args[0].equals("REGISTER")) {
                register (args);
            } else {
                throw new Exception ("Unknown command from client");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception ("Unable to open stream from client");
        }
    }

    public User (Socket socket, boolean b) throws Exception {
        //TODO: verify key from user and
        nickName = "test";
        uid = 1;
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
