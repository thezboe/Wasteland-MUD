package world;

/**
 * Movable is an interface which assures that any object capable of moving from
 * room to room has certain commands. It also acts as a marker for both Player
 * and Mobile objects. It extends the DatabaseItem and GearList interfaces, as
 * movables are database items and gear holders and share much in common with
 * GearContainers. The are a GearList, in that respect as well as have a
 * GearList instance variables.
 */
public interface Movable extends DatabaseItem, GearList {

	/**
	 * sendToPlayer() is used to send text to the client of the player's
	 * controller directly if the movable is a player or send text to the mob if
	 * the movable is a mobile. If a mobile, the mobile can react the the text.
	 * 
	 * @param message
	 *            The message to be sent to the Player's console or the mob.
	 */
	public void sendToPlayer(String message);

	/**
	 * Attack is the method called when a Mobile's turn comes up in a combat. A
	 * Mobile looks to it's strategy in order to determine an appropriate
	 * response - For many mobs, this response is to call resolveAttack, and
	 * deal damage to another Movable.
	 * 
	 * @enemy - The Movable to attack.
	 */
	public void attack(Movable enemy);

	/**
	 * The moveToRoom command handles the upkeep of a room movement by shiftign
	 * the Mobile object from one room to another, and then forms the String to
	 * be given to the Mobile after changing rooms. Mobiles need this less than
	 * players, but other behavior might depend on keywords in room
	 * descriptions, or a reaction to other DatabaseObjects in the room.
	 * 
	 * @param destination
	 *            This is a room reference for the destination of the move.
	 */
	public void moveToRoom(Room destination);

	/**
	 * setStat is a universal setter. It accepts an integer and a Trait enum,
	 * which it then uses to change the appropriate private variable
	 * representing the requested stat to be changed. This cuts down on the
	 * number of methods required to set all instance variables.
	 * 
	 * @param value
	 *            The integer value one wishes to set.
	 * @param stat
	 *            The enum member representing the stat the caller wishes
	 *            modified.
	 */
	public void setStat(int value, Trait stat);

	/**
	 * getStat is a universal getter. It accepts a Trait enum, which it then
	 * uses to get the appropriate private variable representing the requested
	 * stat requested. This cuts down on the number of methods required to get
	 * all instance variables.
	 * 
	 * @return int - An int that represents the value of the requested stat.
	 */
	public int getStat(Trait stat);

	/**
	 * use will be called whenever the movable wants to an item that they
	 * currently have. If a player does attempt to use something that they
	 * currently don't have the method will not do anything.
	 * 
	 * @param itemName
	 *            The name of the gear to be used
	 */
	public void use(String itemName);

	/**
	 * This method insures the correct roomId of the movable, in case there is a
	 * synch or serialization issue.
	 * 
	 * @return An int that represents the movables object reference id in the
	 *         database.
	 */
	public int getRoomId();

	/**
	 * getFighting is a small method that is used to prevent a player from
	 * getting into more than one fight at once. A MOB, however, can be attacked
	 * by multiple human players.
	 * 
	 * @return True if fighting, false otherwise
	 */
	public boolean getFighting();

	/**
	 * setFighting will be used once a player enters combat. It pass in a true
	 * value to set to the player's isFighting variable.
	 * 
	 * @param fighting
	 *            Ture if entering a fight, false if ending combat
	 */
	public void setFighting(boolean fighting);
}
