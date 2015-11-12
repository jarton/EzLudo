package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class Chatmessage extends Command {
    private String message;
    private String chatName;

    public Chatmessage(String rawcmd, User user) {
        super(user, rawcmd);
        String cmd[] = rawcmd.split("\\|");
        message = cmd[3];
        chatName = cmd[1];
    }

    public String getMessage() {
        return message;
    }

    public String getChatName() {
        return chatName;
    }
}
