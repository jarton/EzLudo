package no.hig.ezludo.server;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jdr on 31/10/15.
 */
public class Chatroom {
    private Vector<User> users = new Vector<>();
    private String name = "new Chatroom";
    private int id = -1;
    private Logger logger;

    public Chatroom(String name) {
        this.name = name;
        logger = Logger.getLogger("server");
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Vector<User> getUsers() {
        return users;
    }

    public void chatHandler(String cmd, Vector<User> usersClosedSocets) {
        String command[] = cmd.split("\\|");
        if (command[0].equals("CHAT")) {
            synchronized (users) {
                users.stream().parallel().forEach(user -> {
                    try {
                        user.write("CHAT|" + command[1] + command[2] + command[3] + command[4]);
                        logger.log(Level.ALL, cmd);
                    } catch (Exception e) {
                        usersClosedSocets.add(user);
                    }
                });
            }
        } else if (command[0].equals("LEAVE CHAT")) {
            for (User usr : users) {
                String nickname = usr.getNickname();
                if (nickname == command[3]) {
                    users.remove(usr);
                    logger.log(Level.ALL, command[3] + "left chat");
                }
            }
        } else if (command[0].equals("GET USERS")) {
            String response = "GET USERS|" + command[1];
            for (User usr : users) {
                response += "|" + usr.getNickname();
            }
            final String usrList = response;
            synchronized (users) {
                users.stream().parallel().forEach(user -> {
                    try {
                        user.write(usrList);
                    } catch (Exception e) {
                        usersClosedSocets.add(user);
                    }
                });
            }

        }

    }
}
