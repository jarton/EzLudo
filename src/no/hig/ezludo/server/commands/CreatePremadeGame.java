package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * This command class is used when a user creates a new game.
 * In the command handler thread on the server it makes the server spawn a new game for the user
 * who sendt the command.
 * Created by jdr on 12/11/15.
 * @since 12/11/15
 */
public class CreatePremadeGame extends Command {
    private String rawcmd;
    private User user;
    private String gameName;

    /**
     * sends the parameters to parent. Then extracts the game
     * name from the command and saves it.
     * @param rawcmd command string.
     * @param user user who sent command.
     */
    public CreatePremadeGame(String rawcmd, User user) {
        super(user, rawcmd);
        gameName = rawcmd.split("\\|")[1];
    }

    /**
     * getter method for game name
     * @return string game name
     */
    public String getGameName() {
        return gameName;
    }
}
