package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.Game;

/**
 * Created by jdr on 12/11/15.
 */
public class StartNewGame extends Command {

    Game game;

    public StartNewGame(Game game) {
        super(null, "");
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
