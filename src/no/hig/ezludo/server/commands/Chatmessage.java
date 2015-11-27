package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 * @since 12/11/15
 */
public class Chatmessage extends Command {
    private String message;
    private String chatName;

    /**
     *
     * @param rawcmd
     * @param user
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
