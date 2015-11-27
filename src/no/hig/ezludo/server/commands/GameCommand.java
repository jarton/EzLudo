package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * This command class is user for all game commands like roll,
 * move, leave, game chat. When the command handler gets this object it forwards it
 * to the game it belongs to based on id, and the game object handles the command.
 * @author Jardar
 * @since 12/11/15
 */
public class GameCommand extends Command {
    int gameId;

    /**
     * calls super for parameters.
     * extracts the game id from command.
     * @param rawCmd the command string
     * @param user user who sent the command
     */
    public GameCommand(String rawCmd, User user) {
        super(user, rawCmd);
        gameId = Integer.valueOf(rawCmd.split("\\|")[1]);
    }

    /**
     * getter method for game id
     * @return int game id;
     */
    public int getGameId() {
        return gameId;
    }
}
