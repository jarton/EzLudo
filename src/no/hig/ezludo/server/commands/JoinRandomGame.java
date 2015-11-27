package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * This class is used for the handling of users wanting to join a random game. It extends Command, and represents a
 * request. The requests are sequentially handled by the commandHandler in Server.java, when it does it
 * adds the user to the random game que.
 * @author Jardar
 * @since 12.11.15
 */
public class JoinRandomGame extends Command {
    /**
     * This constructor takes a raw command as a parameter, as well as a user object. It sends the information up the
     * hierarchy to the Command class.
     * @param rawcmd the raw command from the user
     * @param user the user
     */
    public JoinRandomGame(String rawcmd, User user) {
        super(user, rawcmd);
    }
}
