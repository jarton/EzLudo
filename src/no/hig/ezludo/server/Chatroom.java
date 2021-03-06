package no.hig.ezludo.server;

import no.hig.ezludo.server.commands.Chatcommand;
import no.hig.ezludo.server.commands.Chatmessage;
import no.hig.ezludo.server.commands.Command;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.HashMap;
import java.util.Vector;

/**
 * represents a chatroom on the server.
 */
public class Chatroom {
    private Vector<User> users = new Vector<>();
    private Vector<User> usersToRemove = new Vector<>();
    private String name = "new Chatroom";
    private HashMap<String, Chatroom> chatroomsMap;
    private final static Logger logger = Logger.getLogger("chatLogger");
    private static java.util.logging.Logger excLogger = java.util.logging.Logger.getAnonymousLogger();

    public Chatroom(String name, HashMap<String, Chatroom> rooms) {
        this.name = name;
        chatroomsMap = rooms;
    }

    /**
     * getter method the name of the chatroom
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * getter method of the users vector
     * @return the users in the chatroom
     */
    public Vector<User> getUsers() {
        return users;
    }

    /**
     *
     * @param cmd
     * @param usersClosedSocets
     */
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
                    } catch (Exception e) {
                        excLogger.log(java.util.logging.Level.SEVERE, "an exception was thrown", e);
                        usersClosedSocets.add(user);
                        usersToRemove.add(user);
                        logger.warn(user.getNickname() + "left the chatroom");
                    }
                });
                logger.warn(cmd.getRawCmd());
            }
            removeUsers();
        } else if (cmd instanceof Chatcommand) {
            users.remove(cmd.getUser());
            writeUsers(usersClosedSocets);
            logger.warn(cmd.getUser().getNickname() + "left chat");
        }
        if (!name.equals("lobby"))
            deleteChatroom();
    }

    /**
     *
     * @param usersClosedSocets
     */
    public void writeUsers(Vector<User> usersClosedSocets) {
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
                    excLogger.log(java.util.logging.Level.SEVERE, "an exception was thrown", e);
                    if (usersClosedSocets != null)
                        usersClosedSocets.add(user);
                    usersToRemove.add(user);
                    logger.warn(user.getNickname() + "left the chatroom");
                }
            });
        }
        removeUsers();
    }

    /**
     *
     */
    public void removeUsers() {
        synchronized (usersToRemove) {
            usersToRemove.stream().parallel().forEach(user-> {
                users.remove(user);
            });
        }
        usersToRemove.clear();
    }

    /**
     *
     */
    public void deleteChatroom() {
        new Thread (()->{
            while (true) {
                users.stream().parallel().forEach(user -> {
                    try {
                        user.write("ping");
                    } catch (Exception e) {
                        usersToRemove.add(user);
                        excLogger.log(java.util.logging.Level.SEVERE, "an exception was thrown", e);
                    }
                });
                removeUsers();
                if (users.isEmpty()) {
                    chatroomsMap.remove(name);
                    logger.warn("deleted chatroom because no users left");
                    break;
                }
                writeUsers(null);

                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    excLogger.log(java.util.logging.Level.SEVERE, "an exception was thrown", e);
                }
            }
        }).start();
    }
}
