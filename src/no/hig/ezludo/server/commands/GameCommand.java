package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class GameCommand extends Command {

    public GameCommand(String rawCmd, User user) {
        super(user, rawCmd);
    }
}
