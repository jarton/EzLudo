package no.hig.ezludo.server;

import java.io.*;
import java.net.Socket;

/**
 * Created by jdr on 29/10/15.
 */
public class LoginHandler {
    BufferedReader buffReader;
    BufferedWriter buffWriter;

    public LoginHandler (Socket socket) throws Exception {
        try {
            buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String args[] = buffReader.readLine().split("[\t]");
            if (args[0].equals("LOGIN")) {
                login (args, socket.getInetAddress().getHostAddress());
            } else if (args[0].equals("REGISTER")) {
                register();
            } else {
                throw new Exception ("Unknown command from client");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception ("socket failed");
        }
    }

    private void login(String args[], String hostName) {
        //TODO: verify user from db, genrate key. update db
        writeToBuffer("LOGIN OK" + " 1234");
    }

    private void register() {
        //TODO: insert user to db, genrate key.
        writeToBuffer("REGISTRATION OK" + " 1234");
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
