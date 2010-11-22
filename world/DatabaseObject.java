package world;

/**
 * DatabaseObject is the main class for basically all objects that are currently
 * in the world. Ever object that is a DatabaseObject will have a unique
 * reference number along with a name, description and a location. Because rooms
 * are also DatabaseObjects then the location will also be a DatabaseObject. It
 * implements DatabaseItem.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public abstract class DatabaseObject implements DatabaseItem {

	private static final long serialVersionUID = 1L;
	private int databaseRef;
	private DatabaseObject location;
	private String name;
	private String description;

	/**
	 * The DatabseObject constructor takes in a String that represents the name
	 * of the object. If any spaces exist in the String, the constructor
	 * converts them to underscores. It also sets the default description of the
	 * item to "You are looking at: " + the name that is passed.
	 * 
	 * @param name
	 *            - A String that represents the name of an object.
	 */
	public DatabaseObject(String name) {
		this.setName(name.replaceAll(" ", "_"));

		this.setDescription("You are looking at: " + this.getName());
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getDatabaseRef() {
		return this.databaseRef;
	}

	@Override
	public void setDatabaseRef(int ref) {
		databaseRef = ref;
	}

	@Override
	public void setDescription(String descrip) {
		this.description = descrip;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void setLocation(DatabaseObject location) {
		this.location = location;
	}

	@Override
	public DatabaseObject getLocation() {
		return this.location;
	}

	@Override
	public String toString() {
		String result = this.getName() + " (DB:" + this.getDatabaseRef() + ")";
		return result;
	}
}
