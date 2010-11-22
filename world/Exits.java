package world;

import java.io.Serializable;

/**
 * Exits is a class that stores Rooms and associates them with a Direction. The
 * exists are then stored in rooms themselves. It implements Serializable.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class Exits implements Serializable {

	private static final long serialVersionUID = 1L;
	private Room north;
	private Room east;
	private Room south;
	private Room west;
	private Room up;
	private Room down;

	/**
	 * This constructor assigns null rooms to the 6 directions.
	 */
	public Exits() {
		north = null;
		east = null;
		south = null;
		west = null;
		up = null;
		down = null;
	}

	/**
	 * This method associates a Room with a direction and stores that
	 * association as an instance variable.
	 * 
	 * @param dir
	 *            - The Direction to associate with a room.
	 * @param destination
	 *            - The Room to associated with a direction.
	 */
	public void setDestination(Direction dir, Room destination) {
		if (dir == Direction.NORTH) {
			north = destination;
		} else if (dir == Direction.EAST) {
			east = destination;
		} else if (dir == Direction.SOUTH) {
			south = destination;
		} else if (dir == Direction.WEST) {
			west = destination;
		} else if (dir == Direction.UP) {
			up = destination;
		} else if (dir == Direction.DOWN) {
			down = destination;
		}

	}

	/**
	 * This method returns the room associated with the passed direction.
	 * 
	 * @param dir
	 *            - The Direction to see what room is associated with it.
	 * @return - A Room that is associated with the direction that was passed
	 *         in.
	 */
	public Room getDestination(Direction dir) {
		if (dir == Direction.NORTH) {
			return north;
		} else if (dir == Direction.EAST) {
			return east;
		} else if (dir == Direction.SOUTH) {
			return south;
		} else if (dir == Direction.WEST) {
			return west;
		} else if (dir == Direction.UP) {
			return up;
		} else if (dir == Direction.DOWN) {
			return down;
		}
		return null;
	}

	/**
	 * This method returns a String representation of the exits.
	 * 
	 * @return - A String that represents the exits stored here.
	 */
	public String getExits() {
		String result = "Exits: ";
		if (north != null)
			result += "north ";
		if (east != null)
			result += "east ";
		if (south != null)
			result += "south ";
		if (west != null)
			result += "west ";
		if (up != null)
			result += "up ";
		if (down != null)
			result += "down ";
		if (result == "Exits: ")
			result += "None";
		return result;
	}

}
