package server;

import java.io.*;
import java.net.*;

import world.Dreadnaught;
import world.Gunner;
import world.Interpreter;
import world.Room;

/**
 * server.Client is created whenever a new player network.Client connects to the
 * server.MudServer. It will call doInit which will request a socket from the
 * server from which it can use to interact with the server, world and other
 * players. The run() method for server.Client's thread call the necessary
 * methods during init and gameplay and disconnect from the MudServer. The Send
 * and receive msg's are controlled in its run command. When a server.Client
 * receives a text command while in game mode, it sends the text to the
 * world.Interpreter's processCommand method.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrie
 * 
 */
public class Client implements Runnable {

	private Thread thread;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private volatile server.ClientState state;
	private world.Player player;
	private MudServer server;
	private Interpreter interpreter;

	/**
	 * Client constructor will set the instance variable of socket to the socket
	 * that is passed in. This socket will come from the server when the
	 * connection is accepted.
	 * 
	 * @param socket
	 *            - The Socket to receive and send communications.
	 * @param server
	 *            - The MudServer the client is associated with.
	 * 
	 */
	public Client(Socket socket, MudServer server) {
		this.interpreter = Interpreter.getInstance();
		this.socket = socket;
		this.server = server;
	}

	/**
	 * This method initializes the output and input stream variables and starts
	 * the Client thread.
	 */
	public void start() {
		this.state = ClientState.INIT;

		try {
			this.output = new ObjectOutputStream(socket.getOutputStream());
			this.input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
		this.thread = new Thread(this);
		thread.start();
	}

	/**
	 * sendReply is a simple method that will take in a string and will print
	 * the string to the screen. Based on what command was called it will print
	 * accordingly, ie all players or just current.
	 * 
	 * @param reply
	 *            String of what to print
	 */
	public void sendReply(String reply) {
		Communication comm = new Communication(reply);
		try {
			output.writeObject(comm);
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * run is the main method for the client thread. It will run constantly
	 * looking for messages being sent to the client and for any data that the
	 * client is sending out.
	 */
	public void run() {

		try {
			while (state != ClientState.DONE) {

				synchronized (interpreter.getWorld().getLockObject()) {
					while (interpreter.getWorld().threadsLocked()) {
						try {
							interpreter.getWorld().getLockObject().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				if (state == ClientState.INIT)
					doInit();
				else if (state == ClientState.PLAYING)
					listenForInput();
			}
		} catch (SocketException se) {
			shutdown();
		} catch (IOException ioe) {
			shutdown();
		}
	}

	/**
	 * This method gets the current ClientState for the client:
	 * DONE,ERROR,PLAYING,INIT.
	 * 
	 * @return - The ClientState Enum that represents the client's state.
	 */
	public ClientState getState() {
		return state;
	}

	/*
	 * doInit will create a new Client and attempt to connect to the server. If
	 * the connection is accepted a new Client will be made and the thread will
	 * be started.
	 */
	private void doInit() {

		sendReply("|          :::       :::     :::      :::::::: ::::::::::: ::::::::::    |\n"
				+ "|         :+:       :+:   :+: :+:   :+:    :+:    :+:     :+:            |\n"
				+ "|        +:+       +:+  +:+   +:+  +:+           +:+     +:+             |\n"
				+ "|       +#+  +:+  +#+ +#++:++#++: +#++:++#++    +#+     +#++:++#         |\n"
				+ "|      +#+ +#+#+ +#+ +#+     +#+        +#+    +#+     +#+               |\n"
				+ "|      #+#+# #+#+#  #+#     #+# #+#    #+#    #+#     #+#                |\n"
				+ "|      ###   ###   ###     ###  ########     ###     ##########          |\n"
				+ "|            :::            :::     ::::    ::: :::::::::                |\n"
				+ "|           :+:          :+: :+:   :+:+:   :+: :+:    :+:                |\n"
				+ "|          +:+         +:+   +:+  :+:+:+  +:+ +:+    +:+                 |\n"
				+ "|         +#+        +#++:++#++: +#+ +:+ +#+ +#+    +:+                  |\n"
				+ "|        +#+        +#+     +#+ +#+  +#+#+# +#+    +#+                   |\n"
				+ "|       #+#        #+#     #+# #+#   #+#+# #+#    #+#                    |\n"
				+ "|      ########## ###     ### ###    #### #########                      |\n");

		sendReply("Created by:  MUD Anonymous\nRoss Bottorf, Matt Turner, Zachary Boe & Jonathan Perrine");

		sendReply("Type "
				+ '"'
				+ "commands"
				+ '"'
				+ " to see a list of available game commands. It will help you gain some knowledge of how to play the game.");

		boolean playerConfirmed = false;

		// confirm player
		while (!playerConfirmed) {

			String name = "", tempName = "", password = "";

			// get player name
			while (name == null || name.length() < 1 || name.equals("")) {

				while (tempName.equals("") || tempName.equals("commands")) {
					this.sendReply("Input your player name:\n" + "(or type "
							+ '"' + "new" + '"' + " for new player):");
					tempName = receiveCommand();
				}

				if (!validEntry(tempName) && !tempName.equalsIgnoreCase("new"))
					tempName = "";

				// if new player, get name and password
				if (tempName.equals("new")) {

					String newName = "";

					while (newName.equals("") || newName.equals("commands")) {
						sendReply("What player name would you like?\n"
								+ "(please no spaces and cannot be a command keyword,\n"
								+ "type " + '"' + "commands" + '"'
								+ " to see a list of commands)");
						newName = receiveCommand();
					}

					if (interpreter.getWorld().nameExists(newName)) {
						this.sendReply("That name is reserved.");
						newName = "";
					}

					if (!newName.equals("") && this.validEntry(newName)) {
						this.getNewPlayerPasswordAndAddToWorld(newName);
						return;
					} else {
						newName = "";
						tempName = "";
					}
				}

				name = tempName;
			}

			// get password of existing player
			while (password.equals("")) {
				this.sendReply("Please type in your password:");
				password = receiveCommand();
			}

			// confirm player
			if (interpreter.getWorld().confirmPlayer(name, password)) {
				if (this.loadPlayerFromFile(name)) {
					this.addToWorld();
					playerConfirmed = true;
				}
			} else {
				this.sendReply("Invalid user name or password.");
			}
		}
	}

	/*
	 * This private method listens for incoming messages from remote client,
	 * processes quit commands or ooc commands; otherwise, it sends the command
	 * to the intepreter.
	 */
	private void listenForInput() throws IOException, SocketException {

		String textIn;

		textIn = receiveCommand();

		if (textIn.equals("quit")) {
			shutdown();
		} else if (textIn.toLowerCase().indexOf("ooc") == 0) {
			// if command text is ooc then message all players.
			textIn = textIn.substring(3);
			server.messageAllClients("chat <ooc>" + this.player.getName()
					+ " says: " + textIn);
		} else if (!textIn.equals("") || textIn != null) {
			// send command to interpreter
			interpreter.processCommand(this.player, textIn);
		}
	}

	/*
	 * This private method prepares the run method for shutdown.
	 */
	private void shutdown() {
		state = ClientState.DONE;
		// remove this client from the list of clients
		Client temp = this;
		MudServer.getClients().remove(this);
		if (temp != null) {
			if (temp.player != null) {
				temp.server.messageAllClients(temp.player.getName()
						+ " disconnected.");
				temp.player.setClient(null);
				((Room) temp.player.getLocation()).remove(this.player.getName()
						.toLowerCase());
				// temp.player.setLocation(null);
				interpreter.getWorld().savePlayer(temp.player);

				// remove player's name from list of logged on players.
				interpreter.getWorld().removeLoggedOn(
						temp.player.getName().toLowerCase());
			}
			temp = null;
		}
	}

	/*
	 * recieveCommand will take in a player ID and a action. The player ID is
	 * the id of the player that sent the command and the action is what command
	 * they sent. The method will take the proper action based on what the
	 * command is, after the command is sent to the interpreter and the world
	 * does the action.
	 * 
	 * @return - A String that represents the text passed in the command.
	 */
	private String receiveCommand() {
		try {
			String temp = ((Communication) this.input.readObject()).getText();
			if (temp == null)
				return "";
			else if (temp.equalsIgnoreCase("quit")) {
				shutdown();
				return "";
			} else if (temp.equalsIgnoreCase("commands")) {
				String result = "";
				for (String command : interpreter.getCommandDescriptions()) {
					result += command + '\n';
				}
				this.sendReply(result);
				return "";
			} else {
				return temp;
			}
		} catch (IOException e) {
			shutdown();
		} catch (ClassNotFoundException e) {
			shutdown();
		}
		return "";
	}

	/*
	 * This private methods compares the first password entered to the confirm
	 * password entered for new players and also determines if the password has
	 * valid characters and length.
	 * 
	 * @return A boolean, true if password is a valid password.
	 */
	private boolean confirmPassword(String firstPassword, String confirmPassword) {
		return !startsWithCommand(firstPassword)
				&& firstPassword.equals(confirmPassword)
				&& !firstPassword.contains(" ") && firstPassword.length() > 5;
				
	}

	/*
	 * This method checks to see if new name has space, already exists, or is a
	 * command return false.
	 */
	private boolean validEntry(String name) {
        
		boolean result = !name.contains(" ") && !this.startsWithCommand(name);
		if (!result) {
			sendReply("That entry is invalid or reserved.");
			return false;
		}
		for (int i = 0; i< name.length();i++) {
			int Ascii = name.charAt(i);
			if (Ascii < 65 || Ascii > 90 && Ascii <97 || Ascii >122){
				sendReply("That entry is invalid or reserved.");
				return false;
			}
		}
		
		
		return true;
	}

	/*
	 * This private method checks to make sure text being entered as names or
	 * passwords are not commands.
	 * 
	 * @return - True if word is a command word.
	 */
	private boolean startsWithCommand(String text) {
		for (String command : interpreter.getCommands()) {
			if (text.equalsIgnoreCase(command)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * This method gets the new player password and adds the player to the
	 * world.
	 */
	private void getNewPlayerPasswordAndAddToWorld(String newName) {

		String firstPassword = "", confirmPassword = "";
		boolean done = false;
		while (!done) {

			// get password.
			sendReply("Please type a six or more character password:\n"
					+ "(no spaces and no commands):");

			firstPassword = receiveCommand();

			// confirm password
			sendReply("Please confirm your password:");
			confirmPassword = receiveCommand();

			// check password
			if (this.confirmPassword(firstPassword, confirmPassword)) {
				// add to world
				this.player = interpreter.getWorld().createPlayer(newName,
						firstPassword);
				if (player != null) {
					this.interpreter.getWorld().savePlayer(this.player);
					String characterClass = "";
					while (!(characterClass.equalsIgnoreCase("dreadnaught") || characterClass
							.equalsIgnoreCase("gunner"))
							&& (state != ClientState.DONE)) {
						this.sendReply("What character class"
								+ " would you like to be:"
								+ " Gunner or Dreadnaught?");
						characterClass = receiveCommand();
					}
					if (state != ClientState.DONE) {
						if (characterClass.equalsIgnoreCase("gunner")) {
							this.player.setClient(this);
							this.player.setCharacterClass(Gunner.getInstance());
						}
						if (characterClass.equalsIgnoreCase("dreadnaught")) {
							this.player.setClient(this);
							this.player.setCharacterClass(Dreadnaught
									.getInstance());
						}
						this.addToWorld();
					} else {
						player = null;
					}
					done = true;
				}
			} else {
				this.sendReply("Invalid password.");
			}
		}
	}

	/*
	 * This method initializes the player instance variable from a loaded file.
	 * 
	 * @return true if sucessfully loaded.
	 */
	private boolean loadPlayerFromFile(String name) {
		this.player = interpreter.getWorld().loadPlayer(name);
		if(this.player!= null){
			this.player.setClient(this);
		}
		return this.player != null;
	}

	/*
	 * This method sets the player instance variable up with the necessary
	 * setting required for gameplay.
	 */
	private void addToWorld() {

		if (this.player != null) {
			interpreter.getWorld().addLoggedOn(
					this.player.getName().toLowerCase());
			this.state = ClientState.PLAYING;
			this.player.moveToRoom((Room) interpreter.getWorld()
					.getDatabaseObject(this.player.getRoomId()));
			((Room) this.player.getLocation()).sendToRoom(this.player.getName()
					+ " enters game.",player);
		}
	}
}