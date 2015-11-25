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

    private String register() {
        try {
            password = Login.getSHA256("testtest", email);
            output.println("REGISTER|" + email + "|" + password + "|" + "testolini");
            output.flush();
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String login() {
        try {
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
            loginSocket = new Socket("127.0.0.1", 6969);
            output = new PrintWriter(new OutputStreamWriter(loginSocket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(loginSocket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* public void startListener() {
        new Thread(() -> {
            while (input != null) {
                String cmd;
                try {
                    while ((cmd = input.readLine()) != null) {
                        String command[] = cmd.split("\\|");
                        if (command[0].equals("CHAT")) {
                            String checkResponse = command[3];
                            assertThat(checkResponse, containsString(testMessage));
                        } else if (command[0].equals("CHAT JOINED")) {
                        } else if (command[0].equals("USERS")) {
                        } else if (command[0].equals("GAME STARTED")) {
                        } else if (command[0].equals("GAME")) {
                            if (command[3].equals("TURN")) {
                            } else if (command[3].equals("ROLL")) {
                            } else if (command[3].equals("MOVE")) {
                            }
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    } */

    public void checkResponse() {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testClientFunctionality() throws Exception {
        setUpLoginConnection();
        String key = login();
        client = new Client(email, password, key);

        output = client.getOutput();
        input = client.getInput();

        //startListener();

        client.joinChatRoom("lobby");

        checkResponse();
        checkResponse();
        client.sendChatMessage(testMessage, "lobby");
        checkResponse();


        //assertThat(checkResponse, containsString(testMessage));
    }

}