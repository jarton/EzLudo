package no.hig.ezludo.server;

import java.util.Vector;

/**
 * Created by jdr on 29/10/15.
 */
public class Game {
    private User players[] = new User[4];
    private String name = "Random Game";
    private int id = -1;

    public Game(User players[]) {
        this.players = players;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
