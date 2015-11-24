package no.hig.ezludo.client;

import org.junit.Test;


import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by piddy on 11/23/15.
 */
public class ClientTest {

    private PrintWriter output;
    private BufferedReader input;
    private Socket mainSocket;
    private String nickName;
    private Client client;
    private String email = "test@test.no";
    private String password = "1234";

    private String registration() {
        try {
            output.printf("REGISTER|%s|%s|%s\n", email, password, "testolini");
            output.flush();
            String feedBack = input.readLine();
            return feedBack;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String login() {
        try {
            Socket loginSocket = new Socket("127.0.0.1", 6969);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(loginSocket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(loginSocket.getInputStream()));

            output.printf("LOGIN|%s|%s\n", "test@test.no", "1234");
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);

            /*if (feedBack.equals("Uknown username/password") {

            }*/
            input.close();
            output.close();
            loginSocket.close();
            String response[] = feedBack.split("\\|");
            if (!feedBack.startsWith("LOGIN OK"))
                fail();
            //return main key
            else return response[1];
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Test
    public void testClientFunctionality() throws Exception {
        String key = login();
        client = new Client(email, password, key);

        output = client.getOutput();
        input = client.getInput();

        String testMessage = "jUnit test message";
        client.sendChatMessage(testMessage, "LOBBY");

        String response = input.readLine();
        String command[] = response.split("\\|");
        assertThat(command[3], containsString(testMessage));

    }

    @Test
    public void testStartListener() throws Exception {

    }

    @Test
    public void testSendGameMessage() throws Exception {

    }

    @Test
    public void testJoinChatRoom() throws Exception {

    }

    @Test
    public void testJoinGameRoom() throws Exception {

    }

    @Test
    public void testJoinRandomGame() throws Exception {

    }

    @Test
    public void testRollDice() throws Exception {

    }

    @Test
    public void testMovePiece() throws Exception {

    }

    @Test
    public void testLeaveChatRoom() throws Exception {

    }

    @Test
    public void testLogout() throws Exception {

    }
}