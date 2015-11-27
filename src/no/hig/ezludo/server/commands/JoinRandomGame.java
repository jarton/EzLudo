package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Command representing a command to join a random game, when this gets handled by the command handler
 * it gets the user and puts it in the random game que.
 * @author Jardar
 * @since 12/11/15
 */
public class JoinRandomGame extends Command {
    /**
     * calls super with the params to set them in parent
     * @param rawcmd String command from user
     * @param user User who sendt the command.
     */
    public JoinRandomGame(String rawcmd, User user) {
        super(user, rawcmd);
    }
}
