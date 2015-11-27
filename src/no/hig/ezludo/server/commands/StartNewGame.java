package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.Game;

/**
 * This class represents the a new game command created by the random game dispatcher that pulls users from the
 * random game que and creates a new game. The game gets wrapped in objects from this class and when the command
 * handler gets this it calls functions in the game to start it.
 * @author Jardar
 * @since 12.11.15
 */
public class StartNewGame extends Command {
    Game game;

    /**
     * sets the game object, the super is not used here since the server created this.
      * @param game the game object to store
     */
    public StartNewGame(Game game) {
        super(null, "");
        this.game = game;
    }

    /**
     * getter method for the game object
     * @return the game object
     */
    public Game getGame() {
        return game;
    }
}
