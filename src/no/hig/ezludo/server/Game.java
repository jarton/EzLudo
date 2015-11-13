package no.hig.ezludo.server;

import no.hig.ezludo.server.commands.StartNewGame;

import java.util.Random;
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

    public void startGame() {
        synchronized (players) {
            for (User player : players)
                try {
                    player.write("GAME STARTED|"+name+"|"+ players[0].getNickname() + "|" +
                            players[1].getNickname() + "|" + players[2].getNickname() + "|" +
                            players[3].getNickname() + "|" + players[4].getNickname());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
