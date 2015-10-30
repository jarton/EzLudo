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
    private static Socket client = null;
    private final static String uname = "test@test.no";
    private final static String pwd = "1234";
    private final static String nickname = "testolini";
    private final static int loginPort = 6969;

    public ServerTest() {

    }

    @Test
    public void testRegistation() {
        try {
            client = new Socket("127.0.0.1", loginPort);
            output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            output.printf("REGISTER|%s|%s|%s\n", uname, pwd, nickname);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            input.close();
            output.close();
            client.close();
            if (!feedBack.startsWith("REGISTRATION OK"))
                fail();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testLogin() {
        try {
            client = new Socket("127.0.0.1", loginPort);
            output = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            output.printf("LOGIN|%s|%s\n", uname, pwd);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            input.close();
            output.close();
            client.close();
            if (!feedBack.startsWith("LOGIN OK"))
                fail();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}