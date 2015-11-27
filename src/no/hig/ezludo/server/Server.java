package no.hig.ezludo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;

import no.hig.ezludo.server.commands.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * This class is the server. it contains lists of all games and users.
 * it listens for login/lobby connections, commands, chat and game invites/actions.
 * @author jdr
 */
public class Server {
	private final static String dbUrl = "jdbc:derby:ezLudoServer;";
	private static Connection database;
	private Vector<User> users = new Vector<>();
   	private Vector<User> usersWaitingForGame = new Vector<>();
	private Vector<User> usersClosedSocets = new Vector<>();
	private Vector<User> usersToadd = new Vector<>();
   	private List<Vector<User>> gameInvites = new ArrayList<>();
	private Vector<Game> games = new Vector<>();
	private LinkedBlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();
   	private ServerSocket loginServerSocket=null;
	private ServerSocket mainSocket =null;
	private HashMap<String, Chatroom> chatRooms = new HashMap<>();
	private static final int loginPortNum = 6969;
	private static final int mainPortNum = 9696;
	private Chatroom lobby;
	private Logger serverLogger;
	private static java.util.logging.Logger logger = java.util.logging.Logger.getAnonymousLogger();


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
		   logger.log(Level.SEVERE, "an exception was thrown", sqlEx);
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
	 * This function creates a thread that checks the random game que and sees if
	 * there is more than 4 people there. IF there is it crates a new game, adds the users and wraps it
	 * in a StartNewGame object and puts it in the command que to be started there.
	 */
	private void randomGameDispatcher() {
		Thread t = new Thread(()->{
			while (true) {
				if (usersWaitingForGame.size()>3) {
					User players[] = {usersWaitingForGame.remove(0), usersWaitingForGame.remove(0),
					usersWaitingForGame.remove(0), usersWaitingForGame.remove(0)};
					Game game = new Game();
					game.addAllPlayers(players);
					games.add(game);
					game.setId(games.indexOf(game));
					try {
						commandQueue.put(new StartNewGame(game));
						serverLogger.info("new random game started");
					} catch (Exception e) {
						logger.log(Level.SEVERE, "an exception was thrown", e);
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
	 * that cleans up the user list on the server. Thread also adds new users to the users list.
	 * It is done this way because the thread loops through the user vector and therefore the list
	 * cant be changed while thats going on.
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
							else if (cmd.startsWith("GAME|"))
								commandQueue.put(new GameCommand(cmd, user));
							else if (cmd.startsWith("CREATE GAME"))
								commandQueue.put(new CreatePremadeGame(cmd, user));
							else if (cmd.startsWith("GAME INVITE"))
								commandQueue.put(new GameInvite(cmd, user));
							else if (cmd.startsWith("GAME START"))
								commandQueue.put(new StartPremadeGame(cmd, user));
							else if (cmd.startsWith("LOGOUT")) {
								usersClosedSocets.add(user);
							}
							serverLogger.info("received command: " + cmd);
						}
					} catch (Exception e) {
						logger.log(Level.SEVERE, "an exception was thrown", e);
					}
				});
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					logger.log(Level.SEVERE, "an exception was thrown", e);
				}
				removeClosedSockets();
				addNewUsers();
			}
		}).start();
	}

	/**
	 * adds users to the user vector. gets called from the worker thread that goes through the user vector after
	 * it has looped through. this is needed to avoid changing the vector while an iterator is going through it.
	 * if not we get an exeption.
	 */
	private void addNewUsers() {
		synchronized (users) {
			while (usersToadd.size()>0)
				users.add(usersToadd.remove(0));
		}
	}

	/**
	 * handles commands depending on the type of command object. Handles chat messages and chat commands and
	 * passes them to the chatroom it is for. If the chatCommand is JOIN a room is gets processed here, this is
	 * because the room may not exist yet and therefore needs to be created. The 3 other command types are join
	 * a random game, start a game, and then game commands. The first two are handled in this function but the
	 * game command gets passed on to the game object.
	 */
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
								logger.log(Level.SEVERE, "an exception was thrown", ex);
							}
						}
					}
					else if (cmd instanceof JoinRandomGame) {
						usersWaitingForGame.add(cmd.getUser());
						serverLogger.info("user " + cmd.getUser().getNickname() +
								" added to random game que ");
					}
					else if (cmd instanceof StartNewGame) {
						((StartNewGame)cmd).getGame().setUpGame();
						((StartNewGame)cmd).getGame().gameCreated(usersClosedSocets);
						((StartNewGame)cmd).getGame().startGame(usersClosedSocets);
						serverLogger.info(" new game started ");
					}
					else if (cmd instanceof GameCommand) {
						games.get(((GameCommand)cmd).getGameId()).gameHandler(cmd, usersClosedSocets);
						serverLogger.info(" game commands recived ");
					}
					else if (cmd instanceof CreatePremadeGame) {
						Game game = new Game();
						games.add(game);
						game.setId(games.indexOf(game));
						game.addOnePlayer(cmd.getUser(), usersClosedSocets);
						game.setName(((CreatePremadeGame)cmd).getGameName());
						game.gameCreated(usersClosedSocets);
						serverLogger.info(" Create premade game command received");
					}
					else if (cmd instanceof GameInvite) {
						GameInvite gameInvite = ((GameInvite)cmd);
						if (gameInvite.getResponse()) {
							if (gameInvite.getChoise().equals("ACCEPT")) {
								Game game = games.get(gameInvite.getGameId());
								try {
								gameInvite.getUser().write("GAME JOINED|" + gameInvite.getGameId() +
										"|" + game.getName());
								game.addOnePlayer(gameInvite.getUser(), usersClosedSocets);
								} catch (Exception e) {
									usersClosedSocets.add(gameInvite.getUser());
									logger.log(Level.SEVERE, "an exception was thrown", e);
								}
							}
						serverLogger.info("player " + gameInvite.getInvitedPlayer() + "reponded and "
							+ gameInvite.getChoise());
						}
						else {
							for (User usr : users) {
								if (usr.getNickname().equals(gameInvite.getInvitedPlayer())) {
									usr.write("GAME INVITE|" + gameInvite.getGameId() +
											"|" + gameInvite.getUser().getNickname());
									serverLogger.info("player " + usr.getNickname() + " was invited to "
											+ games.get(gameInvite.getGameId()).getName());
								}
							}
						}
					}
					else if (cmd instanceof StartPremadeGame) {
						StartPremadeGame startPremade = ((StartPremadeGame)cmd);
						Game game = games.get(startPremade.getGameId());
						game.setUpGame();
						game.startGame(usersClosedSocets);
						serverLogger.info("started premade Game " + game.getName());
					}
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "an exception was thrown", e);
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
				if(usersWaitingForGame.contains(user))
					usersWaitingForGame.remove(user);
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
						   logger.log(Level.SEVERE, "an exception was thrown", e);
					   }
				   }
				   loginServerSocket.close();
			   } catch (Exception e) {
				   logger.log(Level.SEVERE, "an exception was thrown", e);
			   }
		   }).start();
	   } catch (IOException e1) {
		   logger.log(Level.SEVERE, "an exception was thrown", e1);
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
							logger.log(Level.SEVERE, "an exception was thrown", e);
						}
					}
					mainSocket.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "an exception was thrown", e);
				}
			}).start();
		} catch (IOException e1) {
			System.exit(0);
			logger.log(Level.SEVERE, "an exception was thrown", e1);
		}

	}

	/**
	 * starts the server
	 * @param args not used yet.
	 */
   public static void main(String[] args) { Server server;  server = new Server(); }
}
