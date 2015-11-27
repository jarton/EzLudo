package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * This class is the parent of all other command classes. All commands has
 * a user who sendt the command and a command string. The command handler in the server class
 * works on a list of Command objects that extends this class.
 * Created by jdr on 12/11/15.
 * @since 12/11/15
 */
public class Command {
    private User user;
    private String rawCmd;

    /**
     * Constructor for command. This gets called with super from
     * the other command classes that extends this. Sets the user and command string.
     * @param usr the user who sent the command
     * @param rawCmd the raw command string
     */
    public Command(User usr, String rawCmd) {
        user = usr;
        this.rawCmd = rawCmd;
    }

    /**
     * getter method for the user object who sent the command
     * @return User object
     */
    public User getUser() {
        return user;
    }

    /**
     * getter method for the raw cammand string sendt by the server
     * @return string command
     */
    public String getRawCmd() {
        return rawCmd;
    }
}
