package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class CreatePremadeGame extends Command {
    private String rawcmd;
    private User user;
    private String gameName;

    public CreatePremadeGame(String rawcmd, User user) {
        super(user, rawcmd);
        gameName = rawcmd.split("\\|")[1];
    }

    public String getGameName() {
        return gameName;
    }
}
