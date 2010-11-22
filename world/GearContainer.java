package world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GearContainer will act as a bag for the player. It will be able to hold
 * maxSize pieces of gearList. The maxSize is read in when the container is
 * created but can be changed. It extends Gear and implements GearList
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrie
 * 
 */
public class GearContainer extends Gear implements GearList {

	private static final long serialVersionUID = 1L;
	private int maxSize;
	private List<Gear> gearList;
	private boolean canCarry;

	/**
	 * GearList constructor will read in an integer and will use it to set the
	 * maxSize instance variable for the container.
	 * 
	 * @param maxSize
	 *            The maximum amount of items the container can hold
	 */
	public GearContainer(String name, String description, int maxSize,
			boolean canCarry) {
		super(name, description);
		this.setMaxGearCount(maxSize);
		this.gearList = new ArrayList<Gear>();
		this.canCarry = canCarry;
	}

	@Override
	public boolean addGear(Gear item) {

		if (item instanceof GearContainer
				&& !((GearContainer) gearList).canBeCarried()) {
			return false;
		}

		if (this.gearList.size() == this.maxSize) {
			return false;
		}

		if (this.gearList.add(item)) {

			item.setLocation(this);
			return true;
		}

		return false;
	}

	@Override
	public boolean addGear(Movable movableToNotify, Gear gear) {

		if (gear instanceof GearContainer
				&& !((GearContainer) gear).canBeCarried()) {
			movableToNotify.sendToPlayer(gear.getName() + " cannot be added.");
			return false;
		}

		if (this.gearList.size() == this.maxSize) {
			movableToNotify.sendToPlayer(this.getName() + " is full.");
			return false;
		}

		if (!this.addGear(gear)) {
			movableToNotify.sendToPlayer("Cannot add " + gear.getName());
			return false;
		}

		movableToNotify.sendToPlayer(gear.getName() + " added to "
				+ this.getName() + ".");

		return true;
	}

	@Override
	public boolean giveGear(Movable movableToNotify, String itemName,
			String otherName) {

		Gear item = this.getGear(itemName);
		if (item == null) {
			if (movableToNotify != null)
				movableToNotify.sendToPlayer(itemName
						+ " is not in your inventory.");
			return false;
		}

		Room loc = this.getLocation(this.getLocation());

		if (loc == null) {
			System.out.println(this.getName() + " location failure.");
			if (movableToNotify != null)
				movableToNotify.sendToPlayer(this.getName()
						+ " location failure.");
			return false;
		}

		GearList other = null;
		if (World.getInstance().playerExists(otherName)
				&& World.getInstance().playerIsLoggedOn(otherName)) {
			other = World.getInstance().getPlayer(otherName);
		} else if (World.getInstance().mobileExists(otherName)) {
			other = World.getInstance().getMobile(otherName);
		} else {
			if (loc != null) {
				for (Gear roomItem : loc.listGear()) {
					if (roomItem instanceof GearContainer
							&& otherName.equals(roomItem.getName()
									.toLowerCase())) {
						other = (GearContainer) roomItem;
					}
				}
			}
		}

		if (other != null && other.getLocation().equals(loc)) {
			if (!other.addGear(movableToNotify, item)) {
				return false;
			}
		}

		if (movableToNotify != null) {
			if (other == null) {
				movableToNotify.sendToPlayer(otherName + " is not here.");
				return false;
			}

			if (this.gearList.remove(item)) {
				
				movableToNotify.sendToPlayer(item.getName()
						+ " has been removed from your inventory.");
				movableToNotify.sendToPlayer(other.inspect());
				if (other instanceof Player) {
					((Player) other).sendToPlayer(movableToNotify.inspect());
				}
			} else {
				movableToNotify.sendToPlayer(this.getName()
						+ " remove failure: " + item.getName() + " from "
						+ otherName + ".");
				System.out.println(this.getName() + " remove failure: "
						+ item.getName() + " from " + otherName + ".");
			}
		} else {
			this.gearList.remove(item);
		}

		return true;
	}

	@Override
	public Gear getGear(String itemName) {
		for (Gear item : this.gearList) {
			if (item.getName().toLowerCase().equals(itemName.toLowerCase())) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void dropGear(String itemName, Room room, Movable movableToNotify) {
		Gear temp = this.getGear(itemName);
		if (this.gearList.remove(this.getGear(itemName))) {
			room.add(temp);
			temp.setLocation(room);
			movableToNotify.sendToPlayer(temp.getName()
					+ " has been removed from your inventory.");
			room.sendToRoom("" + this.getName() + " drops " + temp.getName());
		}

		return;
	}

	@Override
	public boolean canBeCarried() {
		return this.canCarry;
	}

	@Override
	public int getMaxGearCount() {
		return this.maxSize;
	}

	@Override
	public void setMaxGearCount(int max) {
		this.maxSize = max;
	}

	@Override
	public String inspect() {
		String result = super.getDescription() + "\n";
		if (!this.gearList.isEmpty()) {
			Collections.sort(this.gearList);
			result += "Items:\n";
			for (Gear gearList : this.gearList) {
				result += gearList.getName() + " " + gearList.getDescription()
						+ "\n";
			}
		}
		return result;
	}

	@Override
	public void getDefaultBehavior(Movable movable) {
		movable.sendToPlayer(movable.inspect());
	}

	@Override
	public List<Gear> listGear() {
		Collections.sort(gearList);
		return this.gearList;
	}

	/*
	 * This private helper method gets the first Room that is a
	 * parent/grandparent to this item
	 */
	private Room getLocation(DatabaseObject databaseObject) {

		if (databaseObject == null) {
			return null;
		}

		if (databaseObject instanceof Room) {
			return (Room) databaseObject;
		}

		return this.getLocation(databaseObject.getLocation());
	}
}
