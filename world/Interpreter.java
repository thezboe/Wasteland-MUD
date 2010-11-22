package world;

import java.util.*;

/**
 * Interpreter is the class that will handle all of the commands that are sent
 * over the network. It will contain a HashMap that will hold String's as the
 * key which are the commands and the commands' descriptions as the value. It
 * has a world instance variable that is a reference to the World.getInstance()
 * singleton. The Interpreter class is also a singleton itself, having a private
 * constructor, private static instance variable of itself, and a public static
 * getInstance method.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class Interpreter {

	private Map<String, String> commandList;
	private static Interpreter instance = new Interpreter();
	private World world;

	// This private constructor initializes the command list, it is private so
	// Interpreter can keep its singleton status.
	private Interpreter() {

		this.world = World.getInstance();

		commandList = new HashMap<String, String>();
		commandList
				.put(
						"look",
						"- look: shows description of the room that the player is in, "
								+ "or if an argument is provided, such as an item/player/MOB in the room, "
								+ "it should provide the description of said item/player/MOB). "
								+ "This command gives a 360 degree report of the environment "
								+ "(The player is not assumed to be looking in a specific Direction.");
		commandList.put("north", "- north: moves the player North.");
		commandList.put("south", "- south: moves the player South.");
		commandList.put("east", "- east: moves player east.");
		commandList.put("west", "- west: moves player west.");
		commandList.put("up", "- up: moves player up.");
		commandList.put("down", "- down: moves player down.");
		commandList.put("n", "- n: moves the player North.");
		commandList.put("s", "- s: moves the player South.");
		commandList.put("e", "- e: moves player east.");
		commandList.put("w", "- w: moves player west.");
		commandList.put("u", "- u: moves player up.");
		commandList.put("d", "- d: moves player down.");
		commandList
				.put(
						"ooc",
						"- ooc <message>: Out of Character channel—the basic MUD wide chat command—message goes to everyone currently connected.");
		commandList.put("who", "- who: lists all players that are logged in.");
		commandList
				.put(
						"say",
						"- say: sends a message to all players in the same room as the player executing the command.");
		commandList
				.put("tell",
						"- tell <player> <message>: sends a message to only the player targeted.");
		commandList.put("score",
				"- score: displays the players current status/information.");
		commandList
				.put("give",
						"- give <item> <player>: gives item in your inventory to player/MOB.");
		commandList
				.put(
						"get",
						"- get <item>: gets item from room. Also: get <item> <target>: gets item from player/MOB/item.");
		commandList.put("inventory",
				"- inventory: lists the items that you are carrying.");
		commandList.put("i", "- i: lists the items that you are carrying.");
		commandList.put("drop",
				"- drop <item>: drops an item from your inventory to the room.");
		commandList.put("use",
				"- use <item>: executes the item’s default behavior.");
		commandList
				.put("quit",
						"- quit: allows a player to exit the system. Will not shut MUD down.");
		commandList
				.put(
						"shutdown",
						"- shutdown: saves the MUD’s data and then shuts the system down. (only game administrator's can use this.");
		commandList.put("save", "- save: saves player state in the game.");
		commandList
				.put("describeme",
						"- describeme <description>: sets your (the player's) description.");
		commandList.put("commands",
				"- commands: lists all the commands useable by a player.");
		commandList
				.put(
						"inspect",
						"- inspect <player,mob,item>: lists all the items being held or contained in a player, mob, or other item.");

		commandList.put("attack",
				"- attack <mob>: initializes combat with a MOB (same as kill).");
		commandList.put("kill", "- kill <mob>: initializes combat with a MOB (same as attack).");
		commandList.put("snipe",
				"- snipe <mob>: initializes sniper attack on mob, is a gunner command only.");
		commandList.put("wound",
				"- wound <mob>: initializes wound attack on mob, is a dreadnaught command only.");
		commandList.put("setclass",
				"- setclass <class>: (Gunner or Dreadnaught.)");
		commandList.put("new", "- new: creates new player on log in.");
	}

	/**
	 * processCommand(Player,Sting) will take in a player that is the player
	 * that called the command and a String that will hold the text that the
	 * player entered. It will take the String and search the keys of the
	 * HashMap and if it is found will run the command value associated with it.
	 * If not it will send it to the room that the player is in and search its
	 * HashMap for the command.
	 * 
	 * @param player
	 *            Player object that represents the player that sent the command
	 * @param textCommand
	 *            The string that the player entered
	 */
	public synchronized void processCommand(Player player, String textCommand) {
		Scanner scanner = new Scanner(textCommand);
		if (scanner.hasNext()) {
			String command = scanner.next().trim().toLowerCase();

			// Say
			if (command.equals("say")) {
				if (scanner.hasNextLine()) {
					this.say(player, scanner.nextLine().trim());
				} else {
					player.sendToPlayer("Say what?");
				}

				// Emote
			} else if (command.equals("emote")) {
				if (scanner.hasNextLine()) {
					((Room) player.getLocation()).sendToRoom(player.getName()
							+ " " + scanner.nextLine().trim());
				} else {
					player.sendToPlayer("Emote what?");
				}

				// Social Commands
			} else if (command.equals("rude")) {
				if (scanner.hasNextLine()) {
					String target = scanner.next();

					for (Movable mov : ((Room) player.getLocation())
							.listMovables()) {
						if (mov.getName().equalsIgnoreCase(target)) {
							((Room) player.getLocation()).sendToRoom(player
									.getName()
									+ " makes a rude gesture at " + target + "!");
						}
					}
				} else {
					((Room) player.getLocation()).sendToRoom(player.getName()
							+ " makes a rude gesture!");

				}

				// Commands
			} else if (command.equals("commands")) {
				this.sendCommandDescriptions(player);

				// Who
			} else if (command.equals("who")) {
				this.who(player);

				// Save
			} else if (command.equals("save")) {
				if (world.savePlayer(player)) {
					player.sendToPlayer("Player saved.");
				}

				// Describme
			} else if (command.equals("describeme")) {
				if (scanner.hasNext()) {
					String description = scanner.nextLine().trim();
					player.setDescription(description);
				} else {
					player
							.sendToPlayer("The describeme command should be followed by a description of yourself.");
				}
				// Tell
			} else if (command.equals("tell")) {
				if (scanner.hasNext()) {
					String otherPlayerName = scanner.next().trim();
					if (scanner.hasNextLine()) {
						if (world.playerIsLoggedOn(otherPlayerName)) {
							this.tell(player, world.getPlayer(otherPlayerName),
									scanner.nextLine().trim());
						} else {
							player.sendToPlayer(otherPlayerName
									+ " is not logged on.");
						}
					} else
						player.sendToPlayer("Tell them what?");
				} else
					player.sendToPlayer("Tell who... What?");

				// Look
			} else if (command.equals("look")) {
				if (scanner.hasNext()) {
					look(player, scanner.nextLine().trim());
				} else {
					player.sendToPlayer(((Room) player.getLocation())
							.generateDescription());
				}
				// Shutdown

			} else if (command.equals("inspect")) {
				if (scanner.hasNext()) {
					inspect(player, scanner.nextLine().trim());
				} else {
					player.sendToPlayer("Inspect what?");
				}

			} else if (command.equals("shutdown")
					&& player.getName().equals("administrator")) {
				shutdown();

				// Inventory
			} else if (command.equals("inventory") || command.equals("i")) {
				player.sendToPlayer(player.inspect());
				;

				// Score
			} else if (command.equals("score")) {
				player.sendToPlayer(player.getStats());

				// Drop
			} else if (command.equals("drop")) {
				if (scanner.hasNext()) {
					String itemName = scanner.next().trim();
					player.dropGear(itemName.toLowerCase());
				} else {
					player.sendToPlayer("Drop what?");
				}

				// Use
			} else if (command.equals("use")) {
				if (scanner.hasNext()) {
					String itemName = scanner.next().trim();
					player.use(itemName.toLowerCase());
				} else {
					player.sendToPlayer("Use what?");
				}
				// Get
			} else if (command.equals("get")) {
				if (scanner.hasNext()) {
					String itemName = scanner.next().trim().toLowerCase();
					if (scanner.hasNext()) {
						String target = scanner.next().trim().toLowerCase();
						if (world.playerExists(target)
								&& world.playerIsLoggedOn(target)) {
							if (!world.getPlayer(target).giveGear(
									world.getPlayer(target), itemName,
									player.getName())) {
								player.sendToPlayer(target
										+ " does not have that item.");
							}
							return;
						}
						if (world.mobileExists(target)) {
							if (!world.getMobile(target).giveGear(
									world.getMobile(target), itemName,
									player.getName())) {
								player.sendToPlayer(target
										+ " does not have that item.");
							}
							return;
						}
						for (Gear roomItem : ((Room) player.getLocation())
								.listGear()) {
							if (roomItem instanceof GearContainer
									&& target.equals(roomItem.getName()
											.toLowerCase())) {
								if (((GearContainer) roomItem).giveGear(null,
										itemName, player.getName())) {
									return;
								} else {
									player.sendToPlayer("Does " + target
											+ " have that item?");
									return;
								}
							}
						}
					} else {
						for (Gear roomItem : ((Room) player.getLocation())
								.listGear()) {
							if (roomItem.getName().equalsIgnoreCase(itemName)) {
								if (roomItem instanceof GearContainer
										&& !((GearContainer) roomItem)
												.canBeCarried()) {
									player.sendToPlayer(itemName
											+ " cannot be carried.");
									return;
								}
								player.addGear(roomItem);
								((Room) player.getLocation()).remove(roomItem);
								return;
							}
						}
						player.sendToPlayer(itemName + " is not in the room.");
					}
				} else {
					player.sendToPlayer("Get what?");
				}

				// Give
			} else if (command.equals("give")) {
				if (scanner.hasNext()) {
					String itemName = scanner.next().trim().toLowerCase();
					if (scanner.hasNext()) {
						String target = scanner.next().trim().toLowerCase();
						player.giveGear(player, itemName, target);
						return;
					} else {
						player.sendToPlayer("Give " + itemName
								+ " to who or what?");
					}
				} else {
					player.sendToPlayer("Give what?");
				}

				// Set Class
			} else if (command.equals("setclass")) {
				if (scanner.hasNext()) {
					String characterClass = scanner.next().trim().toLowerCase();
					if (characterClass.equalsIgnoreCase("gunner")) {
						player.setCharacterClass(Gunner.getInstance());
					} else if (characterClass.equalsIgnoreCase("dreadnaught")) {
						player.setCharacterClass(Dreadnaught.getInstance());
					} else {
						player.sendToPlayer(characterClass
								+ " is not a character class.");
					}
				} else {
					player
							.sendToPlayer("Please type a class name: Gunner or Dreadnaught.");
				}

				// Movement
			} else if (command.equals("north") || command.equals("n")) {
				move(player, Direction.NORTH);
			} else if (command.equals("south") || command.equals("s")) {
				move(player, Direction.SOUTH);
			} else if (command.equals("east") || command.equals("e")) {
				move(player, Direction.EAST);
			} else if (command.equals("west") || command.equals("w")) {
				move(player, Direction.WEST);
			} else if (command.equals("up") || command.equals("u")) {
				move(player, Direction.UP);
			} else if (command.equals("down") || command.equals("d")) {
				move(player, Direction.DOWN);

				// Combat related.
			} else if ((command.equals("attack") || command.equals("kill"))
					|| (command.equals("wound") || command.equals("snipe"))) {
				if (player.getFighting() == true) {
					player
							.sendToPlayer("You cannot attack because you are already in battle");
				}
				if (scanner.hasNext()) {
					String toBeAttacked = scanner.next().trim();

					for (Movable i : ((Room) player.getLocation())
							.listMovables()) {
						if (i.getName().equalsIgnoreCase(toBeAttacked)) {

							if (i.getFighting()) {
								player.sendToPlayer(i.getName()
										+ " is already in battle");
								return;
							}
							// Alternate Attack Types
							if (command.equalsIgnoreCase("snipe")) {
								if (player.getCharacterClass().toString()
										.equalsIgnoreCase("Gunner")) {
									((Gunner) player.getCharacterClass())
											.Snipe(player, i);
									return;
								} else {
									player
											.sendToPlayer("You aren't trained in sniping!");
									return;
								}
							}
							if (command.equals("wound")) {
								if (player.getCharacterClass().toString()
										.equalsIgnoreCase("dreadnaught")) {
									((Dreadnaught) player.getCharacterClass())
											.Wound(player, i);
									return;
								} else {
									player
											.sendToPlayer("You aren't trained to Wound like that!");
									return;
								}
							}

							kill(player, i);
							return;
						}

					}
					player.sendToPlayer("Player, " + toBeAttacked
							+ " was not found in the room.");

				} else {
					player
							.sendToPlayer("Who do you want to attack? (Attack <victim>)");
				}

			} else {
				player.sendToPlayer(command + " is not understood.");
			}

		}
	}

	/**
	 * This method returns the Interpreter's world instance variable. Can be
	 * used by the server.Client (when we make all methods in the world package
	 * (except this one and processCommand) package only).
	 * 
	 * @return - A World object that represents the current Mud World.
	 */
	public World getWorld() {
		return this.world;
	}

	/**
	 * getCommands will return a Set of the available commands.
	 * 
	 * 
	 * @return - A Set<String> of the available commands.
	 */
	public Set<String> getCommands() {
		return this.commandList.keySet();
	}

	/**
	 *getInstance() returns a static reference to this Interpreter following
	 * the Singleton pattern. This will be used by server.Client to gain a
	 * reference to the interpreter to which it is sending commands.
	 * 
	 * @return a reference to the Singleton Interpreter.
	 */
	public static Interpreter getInstance() {
		return instance;
	}

	/**
	 * This method returns a list of the available command descriptions.
	 * 
	 * @return A List<String> of the available command description.
	 */
	public List<String> getCommandDescriptions() {
		List<String> commands = new ArrayList<String>();

		for (String command : this.commandList.values()) {
			commands.add(command);
		}
		Collections.sort(commands);
		return commands;
	}

	/*
	 * This method lets a player look at another player, item, or mobile.
	 */
	private void look(Player player, String objName) {

		for (DatabaseObject item : world.getDatabaseObjects()) {
			if (item.getName().toLowerCase().equals(
					objName.toLowerCase().trim())) {
				player.sendToPlayer(item.getDescription());
				return;
			}
		}

		player.sendToPlayer(objName + " is not here.");
	}

	/*
	 * Say allows a player to talk to the other players in the room.
	 * 
	 * @param player The player speaking to everyone else in the room.
	 * 
	 * @param message The player's message to others.
	 */
	private void say(Player player, String message) {
		((Room) this.world.getDatabaseObject(player.getRoomId()))
				.sendToRoom("chat " + player.getName() + " says: " + message);
	}

	/*
	 * Who returns a list of everyone connected to the MUD server.
	 * 
	 * @param player The player object requesting the list.
	 */
	private void who(Player player) {
		String result = "chat Right now, ";
		for (String name : world.getPlayersLoggedOn()) {
			result += " " + name + ",";
		}
		result = result.substring(0, result.length()-1);
		if (world.getPlayersLoggedOn().size() > 1) {
			result += " are connected.";
		} else {
			result += " is connected.";
		}

		player.sendToPlayer(result);
	}

	/*
	 * Tell sends a message between two players, visible only to them.
	 * 
	 * @param player The initiating player of the whisper
	 * 
	 * @param otherPlayer The recipient of the whisper
	 * 
	 * @param message The actual text of the whisper
	 */
	private void tell(Player player, Player otherPlayer, String message) {
		if (otherPlayer != null) {
			otherPlayer.sendToPlayer("chat " + player.getName()
					+ " whispers : " + message);
			player.sendToPlayer("chat You whisper to " + otherPlayer.getName()
					+ ": " + message);
		} else
			player.sendToPlayer("Player does not exist.");
	}

	/*
	 * Closes the MUD Server, after saving the world.
	 */
	private void shutdown() {
		world.saveWorld();
		System.exit(0);
	}

	/*
	 * This method initializes combat with an other.
	 */
	private void kill(Movable attacker, Movable target) {
		CombatManager attack = new CombatManager(attacker, target);
	}

	/*
	 * This method sends command descriptions to the player requesting them.
	 */
	private void sendCommandDescriptions(Player player) {
		String result = "";
		for (String command : this.commandList.values()) {
			result += command + "\n";
			result += command + '\n';
		}
		player.sendToPlayer(result);
	}

	/*
	 * this method moves a player in the specified direction.
	 */
	private void move(Player player, Direction dir) {
		Room destination = ((Room) player.getLocation())
				.getExitDestination(dir);
		if (destination == null) {
			player.sendToPlayer("You can't go that way.");
			return;
		}

		((Room) this.world.getDatabaseObject(player.getRoomId())).sendToRoom(""
				+ player.getName() + " exits " + dir.toString().toLowerCase()
				+ ".", player);

		player.moveToRoom(destination);

		String from = "";
		if (dir == Direction.NORTH) {
			from = "south";
		} else if (dir == Direction.EAST) {
			from = "west";
		} else if (dir == Direction.SOUTH) {
			from = "north";
		} else if (dir == Direction.WEST) {
			from = "east";
		} else if (dir == Direction.DOWN) {
			from = "above";
		} else if (dir == Direction.UP) {
			from = "below";
		}

		((Room) this.world.getDatabaseObject(player.getRoomId())).sendToRoom(""
				+ player.getName() + " enters from " + from + ".", player);
	}

	/*
	 * this method inspectwS the contents of an item container.
	 */
	private void inspect(Player player, String objName) {
		for (DatabaseObject item : world.getDatabaseObjects()) {
			if (item.getName().toLowerCase().equals(
					objName.toLowerCase().trim())
					&& (item instanceof world.GearList)) {
				player.sendToPlayer(((GearList) item).inspect());
				return;
			}
		}

		player
				.sendToPlayer(objName
						+ " does not exist or cannot be inspected.");
	}
}
