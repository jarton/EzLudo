package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 * @since 12/11/15
 */
public class GameInvite extends Command {
    private String rawcmd;
    private User user;
    private boolean response;
    private int gameId;
    private String invitedPlayer;
    private String choise;

    /**
     *
     * @param rawcmd
     * @param user
     */
    public GameInvite(String rawcmd, User user) {
        super(user, rawcmd);
        System.out.println(rawcmd);
        String cmd[] = rawcmd.split("\\|");
        gameId = Integer.parseInt(cmd[1]);
        if (cmd[2].equals("ACCEPT") || cmd[2].equals("DECLINE")) {
            response = true;
            choise = cmd[2];
        }
        else {
            response = false;
            invitedPlayer = cmd[2];
        }
    }

    /**
     * getter method for the bool indicating if the command is a invite or response.
     * @return true if its a response to a invite, false if its a invite
     */
    public boolean getResponse() {
       return response;
    }

    /**
     * getter method for the game id.
     * @return int game id
     */
    public int getGameId() {
        return gameId;
    }

    /**
     *  the name of the player invited
     * @return string player nickname
     */
    public String getInvitedPlayer() {
        return invitedPlayer;
    }

    /**
     * getter method for the type of response from player,
     * @return ACCEPT or DECLINE
     */
    public String getChoise() {
        return choise;
    }
}
