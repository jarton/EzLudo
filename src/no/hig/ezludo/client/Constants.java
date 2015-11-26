package no.hig.ezludo.client;

/**
 * Created by jdr on 13/11/15.
 */
public class Constants {
    private static String serverIP = "127.0.0.1";
    private static final int portNumber = 9696;
    private static final int loginPortNumber = 6969;

    public static String getServerIP () {
        return serverIP;
    }

    public static int getPortNumber () {
        return portNumber;
    }

    public static int getLoginPortNumber () {
        return loginPortNumber;
    }

    public static void setServerIP (String newServerIP) {
        serverIP = newServerIP;
    }
}
