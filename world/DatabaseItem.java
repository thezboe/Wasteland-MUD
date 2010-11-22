package world;

import java.io.Serializable;

/**
 * This Interface represents all the methods required for an item in the world
 * to be a DatabaseItem. It extends Serializable.
 * 
 * @author rbottorf
 * 
 */
public interface DatabaseItem extends Serializable {

	/**
	 * getName returns the name of the DatabaseObject.
	 * 
	 * @return String the DatabaseObject's Name
	 */
	public String getName();

	/**
	 * setName will take in a String and set the object's name field to that
	 * string.
	 * 
	 * @param name
	 *            The new object name.
	 */
	public void setName(String name);

	/**
	 * getDatabaseRef will simple use the reference number that is stored within
	 * the DatabaseObjcet and return it.
	 * 
	 * @return int The DatabaseObject's ref number
	 */
	public int getDatabaseRef();

	/**
	 * setDatabaseRef will take in an int value and use it to set the
	 * DatabaseObjects unique ID number. This should only be ran once per
	 * object, since they should never reset there reference number.
	 * 
	 * @param ref
	 *            The reference number for the object
	 */
	public void setDatabaseRef(int ref);

	/**
	 * setDescription will be called whenever there is a new DatabaseObject
	 * created. It will simple take in a string that is the objects new
	 * description and set it to the description of the current object.
	 * 
	 * @param descrip
	 *            The description of the object
	 */
	public void setDescription(String descrip);

	/**
	 * getDescription will take the current DatabaseObject and return the string
	 * that is the currentDatabaseObjects description and return it.
	 * 
	 * @return The string description of the object
	 */
	public String getDescription();

	/**
	 * setLocation will take the location of the current DatabaseObject, which
	 * is another DatabseObject and set its location to it.
	 * 
	 * @param location
	 *            The DatabaseObject which is the location
	 */
	public void setLocation(DatabaseObject location);

	/**
	 * getLocation will simple take the location or DatabaseObject for the
	 * current instance and return a reference to the DatabaseObject.
	 * 
	 * @return The DatabaseObject that is the location of the current
	 *         DatabaseObject
	 */
	public DatabaseObject getLocation();

	/**
	 * This method overrides the toString method and returns the name and the
	 * database reference of the object. Returns a string in a specific format:
	 * name | Ref #
	 * 
	 * @return A String that represents a description of the object.
	 */
	public String toString();

}
