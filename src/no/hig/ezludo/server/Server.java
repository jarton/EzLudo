package no.hig.ezludo.server;

import Internationalization.Internationalization;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is the server. it contains lists of all games and users.
 * it listens for connections, commands, chat and game actions.
 * @author jdr
 */
public class Server {
   private Internationalization internationalization;
	private final static String dbUrl = "jdbc:derby:ezLudoServer;";
	private static Connection database;
	private Vector<User> users = new Vector<>();
   private Vector<User> usersWaitingForGame = new Vector<>();
	private Vector<User> usersClosedSocets = new Vector<>();
   private Vector<Game> games = new Vector<>();
	private LinkedBlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();
   private ServerSocket loginServerSocket=null;
	private ServerSocket mainSocket =null;
	private Vector<Chatroom> chatRooms = new Vector<>();
	private static final int loginPortNum = 6969;
	private static final int mainPortNum = 9696;

	/**
	 * starts all the listeners on the servers
	 * and connects to the database.
	 */
   Server() {
	   try {
		   database =  DriverManager.getConnection(dbUrl);
	   } catch (SQLException sqlEx) {
		   sqlEx.printStackTrace();
	   }
	   logInListener();
	   connectionListener();
	   serverWorkerThread();
	   startCommandHandler();
   }

	/**
	 * creates a thread in an endless loop that pauses periodically.
	 * reads commands chat and game action from users and saves them so
	 * other functions can handle them as needed. also calls @removeClosedSockets
	 * that cleans up the user list on the server
	 */
	private void serverWorkerThread() {
		new Thread (()->{
			while (true) {
				users.stream().parallel().forEach(user -> {
					try {
						if (user.ready()) {
							String cmd = user.readLine();
							commandQueue.put(cmd);
							System.out.println("command recieved: " + cmd);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				removeClosedSockets();
			}
		}).start();
	}

	private void startCommandHandler() {
		new Thread (()->{
			String msg;
			try {
				while ((msg=commandQueue.take())!=null) {
					String command[] = msg.split("\\|");
					System.out.print("handling command");
					if (command[0].equals("CHAT")) {
						if (command[1].equals("-1")) {
							synchronized (users) {
								users.stream().parallel().forEach(user -> {
									try {
										user.write("CHAT|" + command[2] + command[3] + command[4]);
									} catch (Exception e) {
										usersClosedSocets.add(user);
									}
								});
							}
						}
						else {
							int id = Integer.parseInt(command[1]);
							Vector<User> chatUsers = chatRooms.get(id).getUsers();
							synchronized (chatUsers) {
								chatUsers.stream().parallel().forEach(user -> {
											try {
												user.write("CHAT|" + command[2] + command[3] + command[4]);
											} catch (Exception e) {
												usersClosedSocets.add(user);
											}
										}
								);
							}
						}
					}
					else if (command[0].equals("JOIN CHAT")) {
						int id = Integer.parseInt(command[1]);
						if (chatRooms.get(id) == null) {
							Chatroom chatroom = new Chatroom(command[2]);
							chatRooms.add(chatroom);
							chatroom.setId(chatRooms.indexOf(chatroom));
						}
						for (User usr : users) {
							String nickname = usr.getNickname();
							if (nickname == command[3])
								chatRooms.get(id).getUsers().add(usr);

						}
					}
					else if (command[0].equals("LEAVE CHAT")) {
						int id = Integer.parseInt(command[1]);
						for (User usr : users) {
							String nickname = usr.getNickname();
							if (nickname == command[3])
								chatRooms.get(id).getUsers().remove(usr);

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * removes clients from the list on the server if their socket is closed/has an io exception
	 * function is called from the server working thread which runs every so often.
	 */
	private void removeClosedSockets() {
		synchronized (users) {
			usersClosedSocets.stream().parallel().forEach(client -> {
				users.remove(client);
			});
		}
		usersClosedSocets.clear();
	}

	/**
	 * a serversocket that listens for users who wish to log in. a new thread is spawned for each connection
	 * and then creates a loginHandler that logs the user in or registers the user.
	 */
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

	/**
	 * when the user has logged in they connect to this serversocket. When each user connects they
	 * get their own thread witch spawns a user object and adds them to the list on the server.
	 * the user object takes care of verifying that they have already logged in.
	 */
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
