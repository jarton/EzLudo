package no.hig.ezludo.server;

import java.util.Vector;

/**
 * Created by jdr on 31/10/15.
 */
public class Chatroom {
    private Vector<User> users = new Vector<>();
    private String name = "new Chatroom";
    private int id = -1;

    public Chatroom(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Vector<User> getUsers() {
        return users;
    }
}
