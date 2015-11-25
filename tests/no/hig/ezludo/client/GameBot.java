package no.hig.ezludo.client;

import java.io.*;
import java.net.Socket;
import java.util.Random;

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
    private String spam[] = {"Ella ella eh eh eh, Under my umbrella!! Rihanna :)","Amen.", "hei","Shine bright like a diamond! Shine bright like a diamond!",
            "Jeg er Alfa og Omega, den f�rste og den siste, begynnelsen og enden.","Bibelen er veikartet, Sannvittigheten kompasset.",
            "hva skjer?","Chicken McNuggets oyeah yeah yeah yeah yeah", "lol funny", "I kissed a girl and I liked it, the taste of her cherry chapstick.", "ludo anyone", "ludy hype", "noen eg kan utfordre?",
    "haha", "riot", "liker kinasjakk bedre", "hahahha", "join min chat da vell",  ":D", "nice", "\\^.^/", "plox add me",
    "dette spillet suger", "yoyoyo", "noen som liker kylling?","Boom, boom, boom. Even brighter than the moon, moon, moon. My Katy Perry", "..................................HEI", "jeg heter per",
    "jeg er best i ludo", "noen utFordre MEG!!!!!","Baby, you're a firework. Come on, let your colours burst. Make 'em go, Aah, aah, aah"};

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

            output.println("JOIN CHAT|lobby");
            output.flush();

            output.println("JOIN RANDOM");
            output.flush();

            //chatSpammer();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startListener() {
        int squares[] = new int [5];

        squares[0] = 0;
        squares[1] = 0;
        squares[2] = 0;
        squares[3] = 0;

        int piece = 0;
        int maxSquare = 59;

        new Thread (()->{
            while (input!=null) {
                String cmd;
                try {
                    while ((cmd=input.readLine())!=null) {
                        Thread.sleep(100);
                        String command[] = cmd.split("\\|");
                        if (command[0].equals("GAME STARTED")) {
                            System.out.println(nickname + "game started");
                        } else if (command[0].equals("GAME")) {
                            if (command[3].equals("TURN") && command[4].equals(nickname)) {
                                System.out.println(nickname + " turn");
                                output.println(command[0] + "|" + command[1] + "|"
                                        + command[2] + "|ROLL");
                                output.flush();
                            }
                            else if (command[3].equals("ROLL") && command[4].equals(nickname)) {
                                squares[4] = Integer.parseInt(command[5]);

                                for (int i = 0; i < 4; i++) {
                                    if (squares[i] < 59) {
                                        output.println(command[0] + "|" + command[1] + "|"
                                                + command[2] + "|MOVE|" + String.valueOf(i));
                                        output.flush();
                                        System.out.println(nickname + " sendt move piece " + i);
                                        break;
                                    }
                                }

                                if (command[3].equals("ROLL") && command[4].equals(nickname) && command[5].equals("6")) {
                                    output.println(command[0] + "|" + command[1] + "|"
                                            + command[2] + "|ROLL");
                                    output.flush();
                                }
                            }
                            else if (command[3].equals("MOVE") && command[4].equals(nickname)) {
                                squares[Integer.parseInt(command[5])] = Integer.parseInt(command[6]);
                                System.out.println(nickname + " moved  to" + command[6]);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void chatSpammer() {
        Thread t = new Thread(()->{
            while (true) {
                Random rand = new Random();
                int n = rand.nextInt(21) + 0;
                output.println("CHAT|lobby|" + nickname + "|" + spam[n]);
                output.flush();
                try {

                    int i = rand.nextInt(20000) + 5000;
                    Thread.currentThread().sleep(i);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
