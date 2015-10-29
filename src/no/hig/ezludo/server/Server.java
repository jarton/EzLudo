package no.hig.ezludo.server;

import Internationalization.Internationalization;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by jdr on 29/10/15.
 */
public class Server {
   private Internationalization internationalization;
   private Vector<User> users = new Vector<>();
   private Vector<User> usersWaitingForGame = new Vector<>();
   private Vector<Game> games = new Vector<>();
   private ServerSocket loginServerSocket=null;
	private ServerSocket mainSocket =null;
	private static final int loginPortNum = 6969;
	private static final int mainPortNum = 9696;

   Server() {

   }

   private void logInListener() {
	   try {
		   loginServerSocket = new ServerSocket(loginPortNum);
		   new Thread(() -> {
			   Socket socket;
			   try {
				   while ((socket = loginServerSocket.accept()) != null) {
					   try {
						   LoginHandler handler = new LoginHandler(socket);
					   } catch (Exception e) {
							e.printStackTrace();
					   }
				   }
				   loginServerSocket.close();
			   } catch (Exception e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   }
		   }).start();
	   } catch (IOException e1) {
		   e1.printStackTrace();
		   System.exit(0);
	   }
   }

	private void startConnectionListener() {
		try {
			mainSocket = new ServerSocket (mainPortNum);
			new Thread (() -> {
				Socket socket;
				try {
					while ((socket = mainSocket.accept()) != null) {
						try {
							// if key does not match socket it closes.
							User user = new User(socket);
							synchronized(usersWaitingForGame) {
								usersWaitingForGame.add(user);
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
