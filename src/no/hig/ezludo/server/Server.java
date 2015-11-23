package no.hig.ezludo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import no.hig.ezludo.server.commands.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class is the server. it contains lists of all games and users.
 * it listens for connections, commands, chat and game actions.
 * @author jdr
 */
public class Server {
	private final static String dbUrl = "jdbc:derby:ezLudoServer;";
	private static Connection database;
	private Vector<User> users = new Vector<>();
   private Vector<User> usersWaitingForGame = new Vector<>();
	private Vector<User> usersClosedSocets = new Vector<>();
	private Vector<User> usersToadd = new Vector<>();
   private Vector<Game> games = new Vector<>();
	private LinkedBlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();
   private ServerSocket loginServerSocket=null;
	private ServerSocket mainSocket =null;
	private HashMap<String, Chatroom> chatRooms = new HashMap<>();
	private static final int loginPortNum = 6969;
	private static final int mainPortNum = 9696;
	private Chatroom lobby;
	private Logger serverLogger;


	/**
	 * starts all the listeners on the servers
	 * and connects to the database. Also sets up the lobby chat and
	 * creates a logger.
	 */
   Server() {
	   String log4jConfPath = "src/no/hig/ezludo/server/log4j.properties";
	   PropertyConfigurator.configure(log4jConfPath);
	   serverLogger = Logger.getLogger("Server");
	   try {
		   database =  DriverManager.getConnection(dbUrl);
	   } catch (SQLException sqlEx) {
		   sqlEx.printStackTrace();
	   }
	   lobby = new Chatroom("lobby", chatRooms);
	   chatRooms.put("lobby", lobby);
	   logInListener();
	   connectionListener();
	   serverWorkerThread();
	   startCommandHandler();
	   randomGameDispatcher();
   }

	/**
	 * When more than 4 people have asked for a random game this function
	 * creates one.
	 */
	private void randomGameDispatcher() {
		Thread t = new Thread(()->{
			while (true) {
				if (usersWaitingForGame.size()>3) {
					User players[] = {usersWaitingForGame.remove(0), usersWaitingForGame.remove(0),
					usersWaitingForGame.remove(0), usersWaitingForGame.remove(0)};
					Game game = new Game(players);
					games.add(game);
					game.setId(games.indexOf(game));
					try {
						commandQueue.put(new StartNewGame(game));
						serverLogger.info("new random game started");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
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
							if (cmd.startsWith("CHAT"))
								commandQueue.put(new Chatmessage(cmd, user));
							else if (cmd.startsWith("JOIN CHAT") || cmd.startsWith("LEAVE CHAT"))
								commandQueue.put(new Chatcommand(cmd, user));
							else if (cmd.startsWith("JOIN RANDOM"))
								commandQueue.put(new JoinRandomGame(cmd, user));
							else if (cmd.startsWith("GAME"))
								commandQueue.put(new GameCommand(cmd, user));
							else if (cmd.startsWith("LOGOUT")) {
								usersClosedSocets.add(user);
							}
							serverLogger.info("received command: " + cmd);
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
				addNewUsers();
			}
		}).start();
	}

	private void addNewUsers() {
		synchronized (users) {
			while (usersToadd.size()>0)
				users.add(usersToadd.remove(0));
		}
	}

	private void startCommandHandler() {
		new Thread (()->{
			Command cmd;
			try {
				while ((cmd=commandQueue.take())!=null) {
					serverLogger.info("handling command " + cmd.getRawCmd());
					if (cmd instanceof Chatmessage) {
						chatRooms.get(((Chatmessage)cmd).getChatName()).chatHandler(cmd, usersClosedSocets);
					} else if (cmd instanceof Chatcommand) {
						String type = ((Chatcommand)cmd).getType();
						if (type.startsWith("LEAVE")) {
							chatRooms.get(((Chatcommand)cmd).getChatName()).chatHandler(cmd, usersClosedSocets);
						}
						else if (type.startsWith("JOIN")) {
							String chatName = ((Chatcommand) cmd).getChatName();

							if (!chatRooms.containsKey(chatName)) {
								Chatroom chatroom = new Chatroom(chatName, chatRooms);
								chatRooms.put(chatName, chatroom);
								serverLogger.info("chatRoom created: " + chatName);
							}
							chatRooms.get(chatName).getUsers().add(cmd.getUser());
							serverLogger.info("user " + cmd.getUser().getNickname() +
									" added to chatroom: " + chatName);
							try {
								cmd.getUser().write("CHAT JOINED|" + ((Chatcommand) cmd).getChatName());
								chatRooms.get(chatName).writeUsers(usersClosedSocets);
							} catch (Exception ex) {
								usersClosedSocets.add(cmd.getUser());
							}
						}
					}
					else if (cmd instanceof JoinRandomGame) {
						usersWaitingForGame.add(cmd.getUser());
						serverLogger.info("user " + cmd.getUser().getNickname() +
								" added to random game que ");
					}
					else if (cmd instanceof StartNewGame) {
						((StartNewGame)cmd).getGame().startGame();
						serverLogger.info(" new game started ");
					}
					else if (cmd instanceof GameCommand) {
						games.get(((GameCommand)cmd).getGameId()).gameHandler(cmd, usersClosedSocets);
						serverLogger.info(" game commands recived ");
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
		synchronized (usersClosedSocets) {
			usersClosedSocets.stream().parallel().forEach(user-> {
				users.remove(user);
				serverLogger.info(user.getNickname() + " removed user with io exc");
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
							synchronized(usersToadd) {
								usersToadd.add(user);
								serverLogger.info(user.getNickname() + " logged in");
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
