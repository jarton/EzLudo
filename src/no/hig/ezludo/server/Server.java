package no.hig.ezludo.server;

import Internationalization.Internationalization;
import com.sun.javafx.binding.Logging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by jdr on 29/10/15.
 */
public class Server {
   private Internationalization internationalization;
	private final static String dbUrl = "jdbc:derby:ezLudoServer;";
	private static Connection database;
	private Vector<User> users = new Vector<>();
   private Vector<User> usersWaitingForGame = new Vector<>();
	private Vector<User> usersClosedSocets = new Vector<>();
   private Vector<Game> games = new Vector<>();
   private ServerSocket loginServerSocket=null;
	private ServerSocket mainSocket =null;
	private static final int loginPortNum = 6969;
	private static final int mainPortNum = 9696;

   Server() {
	   try {
		   database =  DriverManager.getConnection(dbUrl);
	   } catch (SQLException sqlEx) {
		   sqlEx.printStackTrace();
	   }
	   logInListener();
	   connectionListener();
	   serverWorkerThread();
   }

	private void serverWorkerThread() {
		new Thread (()->{
			while (true) {
				users.stream().parallel().forEach(user -> {
					try {
						if (user.ready()) {
							String cmd = user.readLine();
							//TODO: DO THINGS DEPENDING ON THE COMMAND TYPE
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				try {
					Thread.currentThread().sleep(100);
				} catch (Exception e) {
				}
				removeClosedSockets();
			}
		}).start();
	}

	private void removeClosedSockets() {
		synchronized (users) {
			usersClosedSocets.stream().parallel().forEach(client -> {
				users.remove(client);
			});
		}
		usersClosedSocets.clear();
	}

	private void logInListener() {
	   try {
		   loginServerSocket = new ServerSocket(loginPortNum);
		   new Thread(() -> {
			   Socket socket;
			   try {
				   while ((socket = loginServerSocket.accept()) != null) {
					   try {
						   LoginHandler handler = new LoginHandler(socket, database);
					   } catch (Exception e) {
							e.printStackTrace();
					   }
				   }
				   loginServerSocket.close();
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		   }).start();
	   } catch (IOException e1) {
		   e1.printStackTrace();
		   System.exit(0);
	   }
   }

	private void connectionListener() {
		try {
			mainSocket = new ServerSocket (mainPortNum);
			new Thread (() -> {
				Socket socket;
				try {
					while ((socket = mainSocket.accept()) != null) {
						try {
							User user = new User(socket, database);
							synchronized(users) {
								users.add(user);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					mainSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}

	}

   public static void main(String[] args) { new Server(); }
}
