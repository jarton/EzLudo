package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * This class is used for the handling of chat messages received from users. It extends Command, and represents a
 * message. The chat messages are sequentially handled by the commandHandler in Server.java.
 * @author Jardar
 * @since 12.11.15
 */
public class Chatmessage extends Command {
    private String message;
    private String chatName;

    /**
     * This constructor takes a raw command as a parameter, as well as a user object. It splits up the command into
     * individual commands and stores the chat room name and message.
     * @param rawcmd the raw command from the user
     * @param user the user
     */
    public Chatmessage(String rawcmd, User user) {
        super(user, rawcmd);
        String cmd[] = rawcmd.split("\\|");
        message = cmd[3];
        chatName = cmd[1];
    }

    /**
     * getter method for the chatmessage
     * @return string chatmessage
     */
    public String getMessage() {
        return message;
    }

    /**
     * getter method for the chatroom name
     * @return string chat room name
     */
    public String getChatName() {
        return chatName;
    }
}
