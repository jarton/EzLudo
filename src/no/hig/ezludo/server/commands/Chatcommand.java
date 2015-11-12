package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class Chatcommand extends Command{
    private String chatName;
    private String type;

    public Chatcommand(String rawcmd, User user) {
        super(user, rawcmd);
        String cmd[] = rawcmd.split("\\|");
        chatName = cmd[1];
        type = cmd[0];
    }

    public String getChatName() {
        return chatName;
    }

    public String getType() {
        return type;
    }
}
