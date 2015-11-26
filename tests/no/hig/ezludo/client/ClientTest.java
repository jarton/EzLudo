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
    private Socket loginSocket;
    private String nickName;
    private Client client;
    private final String email = "test@test.no";
    private String password = "";
    private final String testMessage = "jUnit test message";

    private void register() {
        setUpLoginConnection();

        password = Login.getSHA256("testtest", email);
        output.println("REGISTER|" + email + "|" + password + "|" + "testolini");
        output.flush();
        String response = checkResponse();
        assertThat(response, containsString("REGISTRATION OK"));
    }

    private String login() {
        try {
            setUpLoginConnection();

            password = Login.getSHA256("testtest", email);
            output.println("LOGIN|" + email + "|" + password);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);

            if (feedBack.equals("Uknown username/password")) {
                register();
                return login();
            }
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

    private void setUpLoginConnection() {
        try {
            if (loginSocket != null) loginSocket.close();
            if (output != null) output.close();
            if (input != null) input.close();

            loginSocket = new Socket("127.0.0.1", 6969);
            output = new PrintWriter(new OutputStreamWriter(loginSocket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(loginSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String checkResponse() {
        try {
            if (input != null) {
                String response = input.readLine();
                String command[] = response.split("\\|");

                if (command[0].equals("CHAT JOINED")) {
                    assertThat(command[1], containsString("lobby"));
                } else if (command[0].equals("CHAT")) {
                    assertThat(command[3], containsString(testMessage));
                } else if (response.startsWith("LOGGED IN")) System.out.println(response);
                else if (command[0].equals("USERS")) assertThat(command[1], containsString("lobby"));
                else if (command[0].equals("REGISTRATION OK")) return command[0];
                else if (command[0].equals("Username Occupied")) fail();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "bottom";
    }

    @Test
    public void testClientFunctionality() throws Exception {
        setUpLoginConnection();
        String key = login();
        client = new Client(key);
        client.listnerThread.interrupt();

        output = client.getOutput();
        input = client.getInput();

        client.joinChatRoom("lobby");

        checkResponse();
        checkResponse();
        client.sendChatMessage(testMessage, "lobby");
        checkResponse();
    }
}