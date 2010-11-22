package world;

import java.util.List;

public interface GearList extends DatabaseItem {

	/**
	 * This method adds a gear item to the GearList without notifying any
	 * movables.
	 * 
	 * @param item
	 *            - The gear item to add.
	 * @return - A boolean, true if added, otherwise false.
	 */
	public boolean addGear(Gear item);

	/**
	 * This method adds gear to the GearList and messages movables when
	 * necessary if can or cannot add item.
	 * 
	 * @param movableToNotify
	 *            - the movable to notify if can or cannot add item.
	 * @param gear
	 */
	public boolean addGear(Movable movableToNotify, Gear gear);

	/**
	 * The method gives gear to other players/mobs/GearLists.
	 * 
	 * @param movableToNotify
	 *            - the movable to notify if can or cannot yield item.
	 * @param itemName
	 *            - The name of the item to give.
	 * @param otherName
	 *            - the name of the other to give the item to.
	 * @return - A boolean that represents whether or not the gear was given
	 *         successfully.
	 */
	public boolean giveGear(Movable movableToNotify, String itemName,
			String otherName);

	/**
	 * This method drops a container's gear with the specified name into the
	 * room that was passed as a parameter.
	 * 
	 * @param itemName
	 *            - The name of the gear to drop.
	 * @param room
	 *            - A room object ot drop the item to.
	 */
	public void dropGear(String itemName, Room room, Movable movableToNotify);

	/**
	 * This methods retrieves a Gear item by using a name reference.
	 * 
	 * @param itemName
	 *            - The name of the gear item.
	 * @return - A Gear object with the passed name.
	 */
	public Gear getGear(String itemName);

	/**
	 * This method notifies whether or not a player or mob can carry this
	 * instance.
	 * 
	 * @return - true if the
	 */
	public boolean canBeCarried();

	/**
	 * getMaxGearCount will simply return the max size of the container.
	 * 
	 * @return An in that represents the max size of the container.
	 */
	public int getMaxGearCount();

	/**
	 * setMaxGearCount will take in an integer value and use the value to set
	 * the max size of the container.
	 * 
	 * @param An
	 *            int that represents the max size of the container.
	 */
	public void setMaxGearCount(int max);

	/**
	 * This method returns the description of the GearList and its contents.
	 * 
	 * @return - A String that represents the GearList's description and its
	 *         contents.
	 */
	public String inspect();

	/**
	 * This method lists the gear contained in this object.
	 * 
	 * @return - A List that represents the gear contained in this object.
	 */
	public List<Gear> listGear();

}
