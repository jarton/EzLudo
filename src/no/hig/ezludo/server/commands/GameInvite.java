package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * Created by jdr on 12/11/15.
 */
public class GameInvite extends Command {
    private String rawcmd;
    private User user;
    private boolean response;
    private int gameId;
    private String invitedPlayer;
    private String choise;

    public GameInvite(String rawcmd, User user) {
        super(user, rawcmd);
        String cmd[] = rawcmd.split("\\|");
        gameId = Integer.parseInt(cmd[1]);
        if (cmd[2] == "ACCEPT" || cmd[2] == "DECLINE") {
            response = true;
            choise = cmd[2];
        }
        else {
            response = false;
            invitedPlayer = cmd[2];
        }
    }

    public boolean getResponse() {
       return response;
    }

    public int getGameId() {
        return gameId;
    }

    public String getInvitedPlayer() {
        return invitedPlayer;
    }

    public String getChoise() {
        return choise;
    }
}
