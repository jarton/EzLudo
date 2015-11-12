package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class JoinRandomGame extends Command {
    private String rawcmd;
    private User user;

    public JoinRandomGame(String rawcmd, User user) {
        super(user, rawcmd);
    }
}
