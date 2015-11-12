package no.hig.ezludo.server;

import no.hig.ezludo.server.commands.Chatcommand;
import no.hig.ezludo.server.commands.Chatmessage;
import no.hig.ezludo.server.commands.Command;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.Vector;

/**
 * Created by jdr on 31/10/15.
 */
public class Chatroom {
    private Vector<User> users = new Vector<>();
    private String name = "new Chatroom";
    private final static Logger logger = Logger.getLogger("chatLogger");

    public Chatroom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Vector<User> getUsers() {
        return users;
    }

    public void chatHandler(Command cmd, Vector<User> usersClosedSocets) {
            Logger.getRootLogger().removeAppender("chatfile");
            FileAppender fa = new FileAppender();
            fa.setName("chatfile");
            fa.setFile("logs/chatrooms/" + name + ".log");
            fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
            fa.setThreshold(Level.WARN);
            fa.setAppend(true);
            fa.activateOptions();
            Logger.getRootLogger().addAppender(fa);
        if (cmd instanceof Chatmessage) {
            synchronized (users) {
                users.stream().parallel().forEach(user -> {
                    try {
                        user.write(cmd.getRawCmd());
                        logger.warn(cmd.getRawCmd());
                    } catch (Exception e) {
                        usersClosedSocets.add(user);
                        users.remove(user);
                        logger.warn(user.getNickname() + "left the chatroom");
                    }
                });
            }
        } else if (cmd instanceof Chatcommand) {
            users.remove(cmd.getUser());
            logger.warn(cmd.getUser().getNickname() + "left chat");
        }

        String response = "USERS|" + name;
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
