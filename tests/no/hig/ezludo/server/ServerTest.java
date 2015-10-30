package no.hig.ezludo.server;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

/**
 * Created by jdr on 30/10/15.
 */
public class ServerTest extends TestCase {

    PrintWriter output;
    BufferedReader input;
    Server server;
    private Socket loginClient = null;
    private Socket client = null;
    private final static String uname = "test@test.no";
    private final static String pwd = "1234";
    private final static String nickname = "testolini";
    private final static int loginPort = 6969;
    private final static int mainPort = 9696;
    private String key;

    public ServerTest() {

    }

    @Test
    public void testRegistation() {
        try {
            loginClient = new Socket("127.0.0.1", loginPort);
            output = new PrintWriter(new OutputStreamWriter(loginClient.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(loginClient.getInputStream()));

            output.printf("REGISTER|%s|%s|%s\n", uname, pwd, nickname);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            input.close();
            output.close();
            loginClient.close();
            if (!feedBack.startsWith("REGISTRATION OK"))
                fail();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        login();
        chat();
    }


    public void login() {
        try {
            loginClient = new Socket("127.0.0.1", loginPort);
            output = new PrintWriter(new OutputStreamWriter(loginClient.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(loginClient.getInputStream()));

            output.printf("LOGIN|%s|%s\n", uname, pwd);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            input.close();
            output.close();
            loginClient.close();
            String response[] = feedBack.split("\\|");
            key = response[1];
            if (!feedBack.startsWith("LOGIN OK"))
                fail();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void chat() {
        try {
            client = new Socket("127.0.0.1", mainPort);
            output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            output.println(key);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            if (!feedBack.startsWith("LOGGED IN"))
                fail();
            output.printf("CHAT|LOBBY|"+nickname+"|HALLO LOL TEST");
            String chat = input.readLine();
            System.out.println(chat);
            input.close();
            output.close();
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}