package no.hig.ezludo.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by jdr on 20/11/15.
 */
public class GameBot {

    private String uname;
    private String nickname;
    private String pwd;
    private String mainKey;
    private Socket socket;
    private int portNumber = 9696;
    private int loginPort = 6969;
    private PrintWriter output;
    private BufferedReader input;

    public GameBot(String uname, String pwd, String nickname) {
       this.uname = uname;
        this.pwd = pwd;
        this.nickname = nickname;

        String login = login();
        if (login.startsWith("LOGIN OK")) {
            try {
                socket = new Socket("127.0.0.1", portNumber);
                output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                startListener();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            String reg = registation();
            if (reg.startsWith("REGISTRATION OK")) {
                login();
                try {
                    socket = new Socket("127.0.0.1", portNumber);
                    output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    startListener();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        joinGame();
    }


    public String registation() {
        try {
            socket = new Socket("127.0.0.1", loginPort);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.printf("REGISTER|%s|%s|%s\n", uname, pwd, nickname);
            output.flush();
            String feedBack = input.readLine();
            input.close();
            output.close();
            socket.close();
            return feedBack;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }


    public String login() {
        try {
            socket = new Socket("127.0.0.1", loginPort);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.printf("LOGIN|%s|%s\n", uname, pwd);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);
            input.close();
            output.close();
            socket.close();
            String response[] = feedBack.split("\\|");
            if (response.length > 1)
                mainKey = response[1];
            return feedBack;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void joinGame() {
        try {

            output.println(mainKey);
            output.flush();
            String feedBack = input.readLine();
            System.out.println(feedBack);

            output.println("JOIN RANDOM");
            output.flush();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startListener() {
        new Thread (()->{
            while (input!=null) {
                String cmd;
                try {
                    while ((cmd=input.readLine())!=null) {
                        String command[] = cmd.split("\\|");
                        if (command[0].equals("CHAT")) {
                            System.out.println("recived chat");

                        } else if (command[0].equals("CHAT JOINED")) {

                        } else if (command[0].equals("USERS")) {

                        } else if (command[0].equals("GAME STARTED")) {
                            System.out.println("game started");
                        } else if (command[0].equals("GAME")) {
                            if (command[3].equals("TURN") && command[4].equals(nickname)) {
                                output.println(command[0] + "|" + command[1] + "|"
                                        + command[2] + "|ROLL");
                                output.flush();
                            }
                            if (command[3].equals("ROLL") && command[4].equals(nickname)) {
                                output.println(command[0] + "|" + command[1] + "|"
                                        + command[2] + "|MOVE|0");
                                output.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
