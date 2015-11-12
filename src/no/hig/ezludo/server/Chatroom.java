package no.hig.ezludo.server;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.InputStream;
import java.util.Vector;

/**
 * Created by jdr on 31/10/15.
 */
public class Chatroom {
    private Vector<User> users = new Vector<>();
    private String name = "new Chatroom";
    private int id = -1;
    private final static Logger logger = Logger.getLogger("chatLogger");

    public Chatroom(String name) {
        this.name = name;
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
            Logger.getRootLogger().removeAppender("chatfile");
            FileAppender fa = new FileAppender();
            fa.setName("chatfile");
            fa.setFile("logs/chatrooms/" + name + ".log");
            fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
            fa.setThreshold(Level.WARN);
            fa.setAppend(true);
            fa.activateOptions();
            Logger.getRootLogger().addAppender(fa);
        if (command[0].equals("CHAT")) {
            synchronized (users) {
                users.stream().parallel().forEach(user -> {
                    try {
                        user.write(cmd);
                        logger.warn(cmd);
                    } catch (Exception e) {
                        usersClosedSocets.add(user);
                        users.remove(user);
                        logger.warn(user.getNickname() + "left the chatroom");
                    }
                });
            }
        } else if (command[0].equals("CHAT LEAVE")) {
            for (User usr : users) {
                String nickname = usr.getNickname();
                if (nickname == command[3]) {
                    users.remove(usr);
                    logger.warn(command[3] + "left chat");
                }
            }
        }

        String response = "USERS|" + command[1] + "|" + command[2];
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
                    users.remove(user);
                    logger.warn(user.getNickname() + "left the chatroom");
                }
            });
        }
    }
}
