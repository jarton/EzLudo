package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class Chatmessage extends Command {
    private String message;
    private int chatroomId;

    public Chatmessage(String rawcmd, User user) {
        super(user, rawcmd);
        String cmd[] = rawcmd.split("\\|");
        message = cmd[4];
        chatroomId = Integer.parseInt(cmd[1]);
    }

    public String getMessage() {
        return message;
    }

    public int getChatroomId() {
        return chatroomId;
    }
}
