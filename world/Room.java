package world;

import java.util.*;

/**
 * Room is the main class that will hold all the information about a single
 * room. Each room will have its own list of rooms, movables, gear, and exits.
 * Room will be the main container for all this objects that can be interacted
 * with through the player. Each room is a DatabaseObject, this will allow for
 * easy access to all rooms and also easy access to the description. It extends
 * DatabaseObject.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class Room extends DatabaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Room> roomList;
	private List<Gear> gearList;
	private Set<String> movables;
	private Exits exits;

	/**
	 * Room will take in a String that will be its name. This name will be
	 * passed all the way to DatabaseObject and will be stored there, which will
	 * allow for easy access for printing purposes. All of the lists will also
	 * be initialized here.
	 * 
	 * @param name
	 *            A String that represents the name of the room.
	 */
	public Room(String name) {
		super(name);

		this.exits = new Exits();
		this.roomList = new ArrayList<Room>();
		this.gearList = new ArrayList<Gear>();
		this.movables = new TreeSet<String>();
	}

	/**
	 * add(Gear) will take in a gear object and attempt to add the gear to the
	 * list of gear for the current room. If the gear was added successfully
	 * than it will return true. If the gear wasn't added than it will return
	 * false.
	 * 
	 * @param gearToAdd
	 *            Gear to be added
	 * @return True if gear was added, false otherwise
	 */
	public boolean add(Gear gearToAdd) {
		// Remove argument object from its location/parent, if it has one.
		if (gearToAdd.getLocation() != null) {
			if (gearToAdd.getLocation() instanceof Room) {
				((world.Room) gearToAdd.getLocation()).remove(gearToAdd);
			}
		}

		// Set hew location/parent.
		gearToAdd.setLocation(this);

		// Add to this Object's list & return the success or failure of the add.
		return gearList.add(gearToAdd);
	}

	/**
	 * add(Room) will take in a room and add that room to the list of rooms.
	 * This room will basically be accessible from the room that addRoom was
	 * called from. It will return true if the room was added, and false if it
	 * wasn't.
	 * 
	 * @param roomToAdd
	 *            Room to be added
	 * @return A boolean that represents whether or not the room was added, true
	 *         if added or false otherwise
	 */
	public boolean add(Room roomToAdd) {
		// Remove argument object from its location/parent, if it has one.
		if (roomToAdd.getLocation() != null) {
			((Room) roomToAdd.getLocation()).remove(roomToAdd);
		}

		// Set hew location/parent.
		roomToAdd.setLocation(this);

		// Add to this Object's list & return the success or failure of the add.
		return roomList.add(roomToAdd);
	}

	/**
	 * add(Movable) will take in a movable object and attempt to add the movable
	 * object to the current list of movable's. If the movable was added
	 * successfully it will return true, if the movable wasn't added it will
	 * return false.
	 * 
	 * @param movableToAdd
	 *            Movable to add
	 * @return A boolean, true if movable was added, false otherwise
	 */
	public boolean add(Movable movableToAdd) {

		// Add to this Object's list & return the success or failure of the add.
		boolean result = this.movables
				.add(movableToAdd.getName().toLowerCase());

		// Set hew location/parent.
		if (result) {
			System.out.println(movableToAdd.getName() + " added to room "
					+ this.getName());
			if (movableToAdd instanceof Player) {
				this.refreshMobiles();
			}
		}

		return result;
	}

	/**
	 * remove(Gear) will take in a gear object and attempt to remove from the
	 * room if the room's list of gear. If the gear was removed successfully
	 * then it will return true. If the gear wasn't in the list then it will
	 * return false.
	 * 
	 * @param gearToRemove
	 *            Gear to be removed
	 * @return True if gear was removed, false otherwise
	 */
	public boolean remove(Gear gearToRemove) {
		gearToRemove.setLocation(null);
		return gearList.remove(gearToRemove);
	}

	/**
	 * remove(Room) will take in a room and attempt to remove the room from the
	 * current list of rooms. If the list of rooms contains the parameter room
	 * then it will be removed and return true, if it isn't in the list than it
	 * will return false.
	 * 
	 * @param roomToRemove
	 *            Room to remove
	 * @return True if room was removed, false otherwise
	 */
	public boolean remove(Room roomToRemove) {
		roomToRemove.setLocation(null);
		return roomList.remove(roomToRemove);
	}

	/**
	 * remove(Movable) will take in a movable name and attempt to remove it from
	 * the current list of movable's. If the list had the movable and it was
	 * removed then it will return true. If the movable wasn't in the list it
	 * will return false.
	 * 
	 * @param movableToRemove
	 *            Movable to remove
	 * @return True if movable was removed, false otherwise
	 */
	public boolean remove(String movableNameToRemove) {
		return this.movables.remove(movableNameToRemove.toLowerCase());
	}

	/**
	 * listGear() will simply return the list of gear that the current room
	 * contains.
	 * 
	 * @return A List of Gear representing the Items in the room.
	 */
	public List<Gear> listGear() {
		return gearList;
	}

	/**
	 * listMovables() will simple return the list of movable's for the current
	 * room.
	 * 
	 * @return List of movable's for the room
	 */
	public List<Movable> listMovables() {
		List<Movable> result = new ArrayList<Movable>();

		for (String name : movables) {

			if (World.getInstance().playerIsLoggedOn(name.toLowerCase())) {
				result.add(World.getInstance().getPlayer(name.toLowerCase()));
			} else if (World.getInstance().mobileExists(name.toLowerCase())) {
				result.add(World.getInstance().getMobile(name.toLowerCase()));
			}

		}

		return result;
	}

	/**
	 * listRooms() will simply return the list of rooms that the current room
	 * has.
	 * 
	 * @return List of rooms for the current room
	 */
	public List<Room> listRooms() {
		return roomList;
	}

	/**
	 * generateDescription will generate a unique description for the current
	 * room. This text will be used whenever a player enters a new room. It will
	 * help give all the rooms character.
	 * 
	 * @return A String that represents the description of the room.
	 */
	public String generateDescription() {
		String result = null;
		result = "<<" + this.getName() + ">>\n";
		result += this.getDescription() + "\n";
		if (!gearList.isEmpty()) {
			result += "Items Detected: ";
			result += gearList.toString();
			result += "\n";
		}

		if (!movables.isEmpty()) {
			result += "The following lifeforms are here: ";

			for (Movable movable : this.listMovables()) {
				result += movable.toString() + " ";
			}

			result += "\n";
		}

		result += exits.getExits();

		return result;
	}

	/**
	 * sendToRoom will take in a string that will be sent to any active player
	 * in the current room. This will display anything that needs to be seen by
	 * the players in the room.
	 * 
	 * @param toSend
	 *            String to display for players in the room
	 */
	public void sendToRoom(String toSend) {

		for (Movable mov : this.listMovables()) {
			mov.sendToPlayer(toSend);
		}
	}

	/**
	 * sendToRoom will take in a string that will be sent to any active player
	 * in the current room. This will display anything that needs to be seen by
	 * the players in the room. It will not send a message to the player passed
	 * in the Player parameter.
	 * 
	 * @param toSend
	 *            String to display for players in the room
	 * @param player
	 *            A Player object that represents the player sending the text,
	 *            used so they do not receive the text.
	 */
	public void sendToRoom(String toSend, Player player) {

		for (Movable mov : this.listMovables()) {
			if (mov != player) {
				System.out.println(mov.getName() + " should receive message.");
				mov.sendToPlayer(toSend);
			}
		}
	}

	/**
	 * This method returns the Room object associated with the Direction passed
	 * into the dir parameter.
	 * 
	 * @param dir
	 *            - A Direction to which a room may be assigned.
	 * @return - The Room assigned to the passed direction.
	 */
	public Room getExitDestination(Direction dir) {
		return exits.getDestination(dir);
	}

	/**
	 * The setExitDestination method will associate a Room to one of this
	 * instances exists.
	 * 
	 * @param dir
	 *            - The direction to associate a room with.
	 * @param destination
	 *            - The Room to associated the direction with.
	 */
	public void setExitDestination(Direction dir, Room destination) {
		exits.setDestination(dir, destination);
	}

	/**
	 * This method refreshes/reestablishes all Mobiles' room assignment to this
	 * room.
	 */
	public void refreshMobiles() {
		for (Movable movable : this.listMovables()) {
			if (movable instanceof Mobile) {
				Mobile mobile = (Mobile) movable;
				Room room = (Room) World.getInstance().getDatabaseObject(
						mobile.getRoomId());
				room.remove(mobile.getName());
				mobile.setLocation(room);
				room.add(mobile);
				System.out.println("room id: " + room.getDatabaseRef());
				for (Gear gear : mobile.listGear()) {
					gear = (Gear) World.getInstance().getDatabaseObject(
							gear.getDatabaseRef());
					gear.setLocation(mobile);
				}
			}
		}
	}

	/**
	 * This method refreshes/reestablishes all Players' room assignment to this
	 * room.
	 */
	public void refreshPlayers() {
		for (Movable movable : this.listMovables()) {
			if (movable instanceof Player) {
				Player player = (Player) movable;
				Room room = (Room) World.getInstance().getDatabaseObject(
						player.getRoomId());
				room.remove(player.getName());
				player.setLocation(room);
				room.add(player);
				System.out.println("room id: " + room.getDatabaseRef());
				for (Gear gear : player.listGear()) {
					gear = (Gear) World.getInstance().getDatabaseObject(
							gear.getDatabaseRef());
					gear.setLocation(player);
				}
			}
		}
	}
}
