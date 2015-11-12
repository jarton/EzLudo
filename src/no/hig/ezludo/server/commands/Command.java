package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class Command {
    private User user;
    private String rawCmd;

    public Command(User usr, String rawCmd) {
        user = usr;
        this.rawCmd = rawCmd;
    }

    public User getUser() {
        return user;
    }

    public String getRawCmd() {
        return rawCmd;
    }
}
