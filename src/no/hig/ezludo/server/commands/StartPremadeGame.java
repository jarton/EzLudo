package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * This class is used for the handling of starting a "premade" game. It extends Command, and represents a request to
 * start a game. The requests are sequentially handled by the commandHandler in Server.java.
 * @author Jardar
 * @since 12.11.15
 */
public class StartPremadeGame extends Command {
    private String rawcmd;
    private User user;
    private int gameId;

    /**
     * This constructor takes a raw command as a parameter, as well as a user object. It splits up the command into
     * individual commands and stores the gameId and sends the user and entire command up the hierarchy to the Command
     * class.
     * @param rawcmd the raw command from the user
     * @param user the user
     */
    public StartPremadeGame(String rawcmd, User user) {
        super(user, rawcmd);
        gameId = Integer.parseInt(rawcmd.split("\\|")[1]);
    }

    /**
     * Standard "getter" for gameId.
     * @return the gameId
     */
    public int getGameId() {
        return gameId;
    }
}
