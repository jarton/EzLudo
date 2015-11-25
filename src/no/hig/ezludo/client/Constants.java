package no.hig.ezludo.client;

/**
 * Created by jdr on 13/11/15.
 */
public class Constants {
    private String serverIP;
    private static int portNumber;
    private static int loginPortNumber;

    public Constants() {
        serverIP="127.0.0.1";
        portNumber = 9696;
        loginPortNumber = 6969;
    }

    public String getServerIP () {
        return serverIP;
    }

    public int getPortNumber () {
        return portNumber;
    }

    public int getLoginPortNumber () {
        return loginPortNumber;
    }

    public void setServerIP (String serverIP) {
        this.serverIP = serverIP;
    }
}
