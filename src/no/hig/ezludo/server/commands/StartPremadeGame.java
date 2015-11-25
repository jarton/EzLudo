package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class StartPremadeGame extends Command {
    private String rawcmd;
    private User user;
    private int gameId;

    public StartPremadeGame(String rawcmd, User user) {
        super(user, rawcmd);
        gameId = Integer.parseInt(rawcmd.split("\\|")[1]);
    }

    public int getGameId() {
        return gameId;
    }
}
