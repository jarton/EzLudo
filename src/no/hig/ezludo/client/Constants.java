package no.hig.ezludo.client;

/**
 * This class contains static data and methods that all client classes may use. It contains a server IP, login port and
 * main port. The server IP will likely be changed when logging in.
 * @author Jardar
 * @since 13.11.15
 */
public class Constants {
    private static String serverIP = "127.0.0.1";
    private static final int portNumber = 9696;
    private static final int loginPortNumber = 6969;

    /**
     * This method is used for retrieving the server IP.
     * @return the server IP
     */
    public static String getServerIP () {
        return serverIP;
    }

    /**
     * This method is used for retrieving the main port number.
     * @return the main port number
     */
    public static int getPortNumber () {
        return portNumber;
    }

    /**
     * This method is used for retrieving the login port number.
     * @return the login port number
     */
    public static int getLoginPortNumber () {
        return loginPortNumber;
    }

    /**
     * This method is used for changing the server IP variable.
     * @param newServerIP the new server IP
     */
    public static void setServerIP (String newServerIP) {
        serverIP = newServerIP;
    }
}
