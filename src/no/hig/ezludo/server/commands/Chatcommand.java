package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class Chatcommand extends Command{
    private String chatName;
    private String type;
    private int chatroomId;

    public Chatcommand(String rawcmd, User user) {
        super(user, rawcmd);
        String cmd[] = rawcmd.split("\\|");
        chatroomId = Integer.parseInt(cmd[1]);
        chatName = cmd[2];
        type = cmd[0];
    }

    public String getChatName() {
        return chatName;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public String getType() {
        return type;
    }
}
