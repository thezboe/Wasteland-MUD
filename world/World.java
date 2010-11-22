package world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * World class will hold all of the objects that are contained in the world. It
 * will have an ArrayList of DatabaseObjects, each object will represent a
 * player or piece of equipment in the world. Each will have a unique id so that
 * there is easy storage in the DatabaseObject class.
 * 
 * @author Matt Turner, Ross Bottorf;;, Zach Boe, Jonathan Perrine
 * 
 */
public class World implements Runnable {

	private transient static World instance = new World();
	private transient Thread saveThread;
	private transient boolean threadsLocked;
	private transient Object lockObject = new Object();

	public static final String ROOTDIR = "C:\\";

	private List<DatabaseObject> databaseArray = new ArrayList<DatabaseObject>();
	private int objectNumbers;
	private Map<String, Player> players = new HashMap<String, Player>();
	private Map<String, Mobile> mobiles = new HashMap<String, Mobile>();
	private Set<String> playersLoggedOn = new TreeSet<String>();

	// This private constructor will initialize necessary variables.
	private World() {
		this.saveThread = new Thread(this);
		this.saveThread.start();
	}

	/**
	 * addToWorld will be called in the WorldFactory. All objects will be hard
	 * coded in the WorldFactory and added using this addToWorld method. It will
	 * check the current list of objects and if it is already on the list will
	 * return false, if it is not in the list it will be added and return true.
	 * 
	 * @param toAdd
	 *            DatabaseObject to add
	 * @return True if object was added, false otherwise
	 */
	public boolean addToWorld(DatabaseObject toAdd) {
		boolean result = databaseArray.add(toAdd);
		if (result) {
			toAdd.setDatabaseRef(objectNumbers);
			objectNumbers++;
			System.out.println(toAdd.getName() + " added to world.");
		}
		return result;
	}

	/**
	 * removeFromWorld will take in a DatabaseObject and attempt to remove it
	 * from the world. This will look in the list of DatabaseObjects and if it
	 * in the list, it will remove it and return true. And if it is not in the
	 * list will return false. This will most likely occur when a player
	 * connects or disconnects.
	 * 
	 * @param toRemove
	 *            DatabaseObject to be removed
	 * @return True if object was removed, false otherwise
	 */
	public boolean removeFromWorld(DatabaseObject toRemove) {
		return databaseArray.remove(toRemove);
	}

	public void replaceWithNew(DatabaseObject toRemove, DatabaseObject toAdd) {
		databaseArray.add(toRemove.getDatabaseRef(), toAdd);
	}

	/**
	 * getDatabaseObject will take in an int that will represent the id of the
	 * DatabaseObject that is being looked for. This method will take the int
	 * and iterate through the list and check all the id's of the objects. If
	 * there is an object with the id, it will be returned if the object is not
	 * found it will return null.
	 * 
	 * @param objectID
	 *            int ID of the object
	 * @return DatabaseObject with the id, or null if not found
	 */
	public DatabaseObject getDatabaseObject(int objectID) {
		for (DatabaseObject dbobj : databaseArray) {
			if (dbobj.getDatabaseRef() == objectID)
				return dbobj;
		}
		return null;

	}

	/**
	 * This method returns the Singleton instance of the world.
	 * 
	 * @return - The Instance of the World
	 */
	public static World getInstance() {
		return instance;
	}

	/**
	 * This getPlayers() method returns a list of players.
	 * 
	 * @return - A List that represents the players registered for the game.
	 */
	public List<Player> getPlayers() {

		List<Player> result = new ArrayList<Player>();

		for (Player player : this.players.values()) {
			result.add(player);
		}

		return result;
	}

	/**
	 * The playerExists(String) method returns true if a name already exists in
	 * the player list.
	 * 
	 * @param name
	 *            - A String that represents the name to check and see if it
	 *            exists.
	 * @return - A boolean that represents whether or not the player exists.
	 */
	public boolean playerExists(String name) {
		return this.players.containsKey(name.toLowerCase());
	}

	/**
	 * This method checks to see if a name is being used in the database.
	 * 
	 * @param newName
	 *            - A String that represents the name to check and see if exists
	 *            or not.
	 * @return A boolean, true if it exists, false if not.
	 */
	public boolean nameExists(String newName) {
		for (DatabaseObject worldObject : getDatabaseObjects()) {
			if (newName.equalsIgnoreCase(worldObject.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to create Rooms, it creates a new room with the
	 * passed name and adds it to the database.
	 * 
	 * @param name
	 *            - A String that represents the name to give the room.
	 * @return - A Room object, the new room created.
	 */
	public Room createRoom(String name) {
		Room room = new Room(name);
		this.addToWorld(room);
		return room;
	}

	/**
	 * The createPlayer(String,String) method creates a player and adds it to
	 * the world.
	 * 
	 * @param name
	 *            - A String that represents the name of the player to add to
	 *            the world.
	 * @param password
	 *            - A String that represents the player's password.
	 * @return - A boolean that represents whether or not the player addition
	 *         was successful.
	 */
	public Player createPlayer(String name, String password) {

		if (this.playerExists(name))
			return null;

		Player temp = new Player(name);
		temp.setPassword(password);
		temp.setLocation((Room) World.getInstance().getDatabaseObject(1));
		if (this.players.put(temp.getName().toLowerCase(), temp) == null) {
			World.getInstance().addToWorld(temp);
			return temp;
		}

		return temp;
	}

	/**
	 * createMobile is called when the MUD world is created. It will take in a
	 * string for a name, string as a description, a starting room and a
	 * strategy for that specific MOB. If the MOB already exists it return null.
	 * If it doesn't exist it will create a new MOB, add it to the current
	 * world, set the strategy, and set the location.
	 * 
	 * @param name
	 *            The name for the MOB
	 * @param description
	 *            The description for the MOB
	 * @param room
	 *            The starting room for the MOB
	 * @param strategy
	 *            The strategy for the specific MOB
	 * 
	 * @return The created MOB, or null if duplicate
	 */
	public Mobile createMobile(String name, String description, Room room,
			Strategy strategy) {

		if (this.nameExists(name.toLowerCase()))
			return null;

		Mobile temp = new Mobile(name);
		if (this.mobiles.put(temp.getName().toLowerCase(), temp) == null) {
			World.getInstance().addToWorld(temp);
			temp.setStrategy(strategy);
			temp.setLocation(room);
			temp.moveToRoom(room);
			temp.setStart(room);
			temp.setDescription(description);
			
			return temp;
		}

		return null;
	}

	/**
	 * addGearToWorld is called to add a piece of gear to the world in a
	 * specific location. It needs to take in a gear reference and a location.
	 * The gear will then be added to the room in the world. It can also take in
	 * a gear container, player or mobile. If any of these is passed in instead
	 * of a room, the gear will then be added to the container, player or MOB.
	 * 
	 * @param gear
	 *            The piece of gear to be added
	 * @param location
	 *            The location to add the gear
	 */
	public void addGearToWorld(Gear gear, DatabaseObject location) {
		World.getInstance().addToWorld(gear);
		if (location instanceof Room) {
			((Room) location).add(gear);
			return;
		}

		if (location instanceof GearContainer) {
			((GearContainer) location).addGear(gear);
		}

		if (location instanceof Player) {
			((Player) location).addGear(gear);
		}
		if (location instanceof Mobile) {
			((Mobile) location).addGear(gear);
		}
		gear.setLocation(location);
	}

	/**
	 * getMobiles is called to return a list of all the current MOB's that are
	 * contained in the world. It will use the hashmap that the world has and
	 * create a list containing all of the MOB's in the current world.
	 * 
	 * @return List of MOB's in the world
	 */
	public List<Mobile> getMobiles() {
		List<Mobile> result = new ArrayList<Mobile>();

		for (Mobile mobile : this.mobiles.values()) {
			result.add(mobile);
		}

		return result;
	}

	/**
	 * getMobile will take in a string that is the name of a mobile that is
	 * being searched for. It will use the mobile hashmap in world and get the
	 * value that is mapped to that specific string or mobile name.
	 * 
	 * @param name
	 *            The name of mobile being searched for
	 * @return The mobile that has the name being passed in
	 */
	public Mobile getMobile(String name) {
		return this.mobiles.get(name.toLowerCase());
	}

	/**
	 * mobileExists is a boolean method that takes in a string and uses it to
	 * determine if there is a mobile in the current world that has that name.
	 * If the name does exist in the world than it will return true, false if
	 * there isn't.
	 * 
	 * @param name
	 *            The name of a MOB being searched for
	 * @return True of MOB exits, false otherwise
	 */
	public boolean mobileExists(String name) {
		return this.mobiles.containsKey(name);
	}

	/**
	 * The confirmPlayer(String,String) method confirms whether or not a player
	 * has registered (exists) in the world.
	 * 
	 * @param name
	 *            - A String that represents the name of the player.
	 * @param password
	 *            - A String that represents the player's password.
	 * @return - A boolean that represents whether or not the player exists in
	 *         the world.
	 */
	public boolean confirmPlayer(String name, String password) {
		if (!this.playerExists(name)) {
			return false;
		}
		return this.getPlayer(name).validatePassword(password);
	}

	/**
	 * The getPlayer method retrieves a player from the world's list of players
	 * by their name. This method does not use case sensitivity, meaning that
	 * any combination of upper and lower case letters will be lower cased and
	 * compared to the key in the Map of players.).
	 * 
	 * @param name
	 *            - A String that represents the player's name to retrieve from
	 *            the world.
	 * @return - A Player object with the same name as the passed parameter.
	 */
	public Player getPlayer(String name) {
		return this.players.get(name.toLowerCase());
	}

	/**
	 * The loadWorld() method loads the world from saved files.
	 * 
	 * @throws WorldNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public void loadWorld() throws WorldNotFoundException {
		boolean loadedFromFile = false;

		try {
			// load database objects
			FileInputStream dFis = new FileInputStream(ROOTDIR
					+ "MudWorld\\databaseArray.txt");
			ObjectInputStream dIn = new ObjectInputStream(dFis);
			this.databaseArray = (List<DatabaseObject>) dIn.readObject();
			this.objectNumbers = this.databaseArray.size();

			// load player list.
			FileInputStream pFis = new FileInputStream(ROOTDIR
					+ "MudWorld\\players.txt");
			ObjectInputStream pIn = new ObjectInputStream(pFis);
			this.players = (Map<String, Player>) pIn.readObject();

			// load player list.
			FileInputStream mFis = new FileInputStream(ROOTDIR
					+ "MudWorld\\mobiles.txt");
			ObjectInputStream mIn = new ObjectInputStream(mFis);
			this.mobiles = (Map<String, Mobile>) mIn.readObject();

			// reset player objects.
			for (Player player : this.getPlayers()) {
				Player temp = loadPlayer(player.getName().toLowerCase());
				if (temp != null) {
					player = temp;
				}
			}

			// reset mobile objects.
			for (Mobile mobile : this.getMobiles()) {
				System.out.println(mobile.getName());
				Mobile temp = (Mobile) this.getDatabaseObject(mobile
						.getDatabaseRef());
				mobile = temp;
				Room room = (Room) this.getDatabaseObject(mobile.getRoomId());
				room.remove(mobile.getName());
				mobile.setLocation(room);
				room.add(mobile);
				System.out.println("room id: " + room.getDatabaseRef());
				for (Gear gear : mobile.listGear()) {
					gear = (Gear) this.getDatabaseObject(gear.getDatabaseRef());
					gear.setLocation(mobile);
				}
			}

			loadedFromFile = true;
		} catch (FileNotFoundException e) {
			System.out
			.println("World file(s) not found."
					+ e.getMessage());
		} catch (IOException e) {
			System.out
			.println("World file(s) not reading correctly."
					+ e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out
			.println("World file(s) not casting correctly."
					+ e.getMessage());
		}
		
		if(!loadedFromFile){
			throw new WorldNotFoundException();
		}
	}

	/**
	 * The printAll() method is used to help test game saves and loads.
	 */
	public void printAll() {
		for (DatabaseObject DB : databaseArray) {
			System.out.println(DB.getName() + "|" + DB.getDatabaseRef() + "|"
					+ DB.getDescription());
		}
	}

	/**
	 * lockThreads is used when ever the certain threads need stopped.It will
	 * set the threadsLocked variable to true.
	 */
	public void lockThreads() {
		this.threadsLocked = true;
	}

	/**
	 * threadsLocked() will simply return true if the threads have been locked.
	 * This will be contained in the boolean threadsLocked variable.
	 * 
	 * @return True if threads are locked and false otherwise.
	 */
	public boolean threadsLocked() {
		return threadsLocked;
	}

	/**
	 * unlockThreads() will be called upon to take all locked threads and unlock
	 * them. This will also make use of the lockObject instance variable.
	 */
	public void unlockThreads() {
		synchronized (lockObject) {
			this.threadsLocked = false;
			this.lockObject.notifyAll();
		}
	}

	/**
	 * Returns the lock object for the World instance.
	 * 
	 * @return - An object used as a lock for threads.
	 */
	public Object getLockObject() {
		return this.lockObject;
	}

	/**
	 * The run method saves the world every 100 seconds.
	 */
	@Override
	public void run() {
		boolean justStarted = true;
		try {
			for (;;) {

				synchronized (this.getLockObject()) {
					while (this.threadsLocked()) {
						try {
							this.getLockObject().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				if (!justStarted) {
					this.saveWorld();
				}
				if (justStarted) {
					justStarted = false;
				}
				System.out.println("Save");
				Thread.sleep(100000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The saveWorld() method saves the world instance.
	 */
	public void saveWorld() {
		this.lockThreads();

		File databaseArray = new File(ROOTDIR + "MudWorld\\databaseArray.txt");

		try {
			FileOutputStream fos;
			fos = new FileOutputStream(databaseArray);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(this.databaseArray);
			out.flush();
			out.close();
			fos.close();
			this.unlockThreads();
		} catch (FileNotFoundException e) {
			File folder1 = new File(ROOTDIR + "MudWorld");
			folder1.mkdir();
			File folder2 = new File(ROOTDIR + "MudWorld\\MudPlayers");
			folder2.mkdir();
			this.unlockThreads();
		} catch (IOException e) {
			this.unlockThreads();
			e.printStackTrace();
		}

		this.lockThreads();
		File players = new File(ROOTDIR + "MudWorld\\players.txt");
		try {
			FileOutputStream pFos;
			pFos = new FileOutputStream(players);
			ObjectOutputStream pOut = new ObjectOutputStream(pFos);
			pOut.writeObject(this.players);
			pOut.flush();
			pOut.close();
			pFos.close();
			this.unlockThreads();
		} catch (FileNotFoundException e) {
			File folder1 = new File("MudWorld");
			folder1.mkdir();
			File folder2 = new File("MudWorld\\MudPlayers");
			folder2.mkdir();
			this.unlockThreads();
		} catch (IOException e) {
			this.unlockThreads();
			e.printStackTrace();
		}

		this.lockThreads();
		File mobiles = new File(ROOTDIR + "MudWorld\\mobiles.txt");
		try {
			FileOutputStream mfos;
			mfos = new FileOutputStream(mobiles);
			ObjectOutputStream mOut = new ObjectOutputStream(mfos);
			mOut.writeObject(this.mobiles);
			mOut.flush();
			mOut.close();
			mfos.close();
			this.unlockThreads();
		} catch (FileNotFoundException e) {
			File folder1 = new File("MudWorld");
			folder1.mkdir();
			File folder2 = new File("MudWorld\\MudPlayers");
			folder2.mkdir();
			this.unlockThreads();
		} catch (IOException e) {
			this.unlockThreads();
			e.printStackTrace();
		}
		
		this.savePlayers();

	}

	/**
	 * The save player method saves a singled player instance.
	 * 
	 * @param player
	 *            - The Player object to save.
	 */
	public boolean savePlayer(Player player) {
		// this.saveWorld();
		// return true;
		this.lockThreads();

		File playerFile = new File(ROOTDIR + "MudWorld\\MudPlayers\\"
				+ player.getName().toLowerCase() + ".txt");

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(playerFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(player);
			out.flush();
			out.close();
			fos.close();
			this.unlockThreads();
			return true;
		} catch (FileNotFoundException e) {
			this.unlockThreads();
			e.printStackTrace();
		} catch (IOException e) {
			this.unlockThreads();
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * The savePlayers() method saves all players in the player list.
	 */
	public void savePlayers() {
		for (Player player : this.getPlayers()) {
			this.savePlayer(player);
		}
	}

	/**
	 * This method lists the DatabaseObjects.
	 * 
	 * @return - A List that represents the database objects.
	 */
	public List<DatabaseObject> getDatabaseObjects() {
		return this.databaseArray;
	}

	/**
	 * loadPlayer will take in a String that is a players name and attempt to
	 * load that player from saved files.
	 * 
	 * @param name
	 *            Player name that is attempted to be loaded
	 * @return The player with the string parameter or null if it doesn't exist
	 */
	public Player loadPlayer(String name) {

		// return this.getPlayer(name);
		try {
			FileInputStream pfis = new FileInputStream(
					"C:\\MudWorld\\MudPlayers\\" + name.toLowerCase() + ".txt");
			ObjectInputStream pIn = new ObjectInputStream(pfis);
			Player temp = (Player) pIn.readObject();
			if (temp != null) {
				this.replaceWithNew(this.getDatabaseObject(temp
						.getDatabaseRef()), temp);
				Room room = (Room) this.getDatabaseObject(temp.getRoomId());
				room.remove(temp.getName());
				temp.setLocation(room);
				room.add(temp);
				System.out.println("room id: " + room.getDatabaseRef());
				this.players.remove(name.toLowerCase());
				this.players.put(name.toLowerCase(), temp);
				for (Gear gear : temp.listGear()) {
					gear = (Gear) this.getDatabaseObject(gear.getDatabaseRef());
				}
				return temp;
			}

		} catch (FileNotFoundException e) {
			System.out.println("Player file not found for " + name + ".");
		} catch (IOException e) {
			System.out.println("Player file not read for " + name + ".");
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found for " + name + ".");
		}

		return null;
	}

	/**
	 * addLoggedOn will take in a string and add it to a list that contains only
	 * the players that are currently logged on to the MUD server.
	 * 
	 * @param name
	 *            The player name that logged on
	 * @return True if the add was successful, false otherwise
	 */
	public boolean addLoggedOn(String name) {
		return this.playersLoggedOn.add(name.toLowerCase());
	}

	/**
	 * removeLoggedOn will take in a string of a player that just logged off and
	 * remove it from the list of players that are logged on.
	 * 
	 * @param name
	 *            Name of the player that logged off
	 * @return True if the remove was successful, false otherwise
	 */
	public boolean removeLoggedOn(String name) {
		return this.playersLoggedOn.remove(name.toLowerCase());
	}

	/**
	 * playerIsLoggedOn will take in a string, and use the string to determine
	 * if that player is currently logged on to the MUD server.
	 * 
	 * @param name
	 *            The name of the player
	 * @return True if player is logged on, false otherwise
	 */
	public boolean playerIsLoggedOn(String name) {
		return this.playersLoggedOn.contains(name.toLowerCase());
	}

	/**
	 * getPlayersLoggedOn will take the current set of players that are logged
	 * on to the MUD server and return it.
	 * 
	 * @return The set of players logged on
	 */
	public Set<String> getPlayersLoggedOn() {
		return this.playersLoggedOn;
	}
}
