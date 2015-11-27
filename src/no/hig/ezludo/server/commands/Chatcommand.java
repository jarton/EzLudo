package no.hig.ezludo.server.commands;

import no.hig.ezludo.server.User;

/**
 * This class is used for the handling of users joining and leaving chat rooms. It extends Command, and stores
 * leave/join requests. The requests are sequentially handled by the commandHandler in Server.java.
 * Created by jdr on 12/11/15.
 * @since 12/11/15
 */
public class Chatcommand extends Command{
    private String chatName;
    private String type;

    /**
     * This constructor takes a raw command as a parameter, as well as a user object. It splits up the command into
     * individual commands and stores the chat room name and type (leave or join).
     * @param rawcmd the raw command from the user
     * @param user the user
     */
    public Chatcommand(String rawcmd, User user) {
        super(user, rawcmd);
        String cmd[] = rawcmd.split("\\|");
        chatName = cmd[1];
        type = cmd[0];
    }

    /**
     * Standard "getter" for the chat room name.
     * @return the chat room name
     */
    public String getChatName() {
        return chatName;
    }

    /**
     * Standard "getter" for the type. The type is either "JOIN CHAT" or "LEAVE CHAT".
     * @return the type
     */
    public String getType() {
        return type;
    }
}
