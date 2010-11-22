package world;

/**
 * Gear is the main class for all equipment in the world. It will hold all the
 * traits of the equipment. This includes any boosts to the agility, strength,
 * toughness, intellect or technique. The item level, which represents the level
 * a player must be at to use an item. is also stored here and can be changed.
 * The name and description for each piece of gear can be set and returned
 * through gear. GearLists and Rooms can store gear. A Player, Mobile, and
 * GearContainer all implement the GearList class and interact with Gear in that
 * fashion. It extends DatabaseObject and implements Comparable.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public abstract class Gear extends DatabaseObject implements Comparable<Gear> {

	private static final long serialVersionUID = 1L;
	private int itemLevel;
	private int agilityMod;
	private int strengthMod;
	private int toughnessMod;
	private int intellectMod;
	private int techniqueMod;

	/**
	 * Gear constructor will take in two strings. The first one is going to be
	 * the name of the piece of gear and the second will be the description for
	 * the piece of equipment.
	 * 
	 * @param String
	 *            name The name of the gear
	 * @param String
	 *            description The description of gear
	 */
	public Gear(String name, String description) {
		super(name);
		setDescription(description);
		this.setItemLevel(1);
	}

	/**
	 * setItemLevel will take in an integer value that will be used to set the
	 * item level for the piece of gear. This is the required level the player
	 * must be to use the gear.
	 * 
	 * @param lvl
	 *            An int that represents the level of the gear
	 */
	public void setItemLevel(int lvl) {
		this.itemLevel = lvl;
	}

	/**
	 * getItemLevel will simple return the level that the current piece of gear
	 * has.
	 * 
	 * @return An int that represents the level of the current gear
	 */
	public int getItemLevel() {
		return this.itemLevel;
	}

	/**
	 * setMod(int, Trait) is a universal setter. It accepts an integer and a
	 * Trait enum, which it then uses to change the appropriate private variable
	 * representing the requested Stat Modifier to be changed. This cuts down on
	 * the number of methods required to set all instance variables.
	 * 
	 * @param value
	 *            The integer value one wishes to set.
	 * @param stat
	 *            The enum member representing the stat mod the caller wishes
	 *            modified.
	 */
	public void setMod(int value, Trait stat) {
		if (stat == Trait.AGILITY)
			agilityMod = value;
		else if (stat == Trait.INTELLECT)
			intellectMod = value;
		else if (stat == Trait.STRENGTH)
			strengthMod = value;
		else if (stat == Trait.TECHNIQUE)
			techniqueMod = value;
		else if (stat == Trait.TOUGHNESS)
			toughnessMod = value;
	}

	/**
	 * getMod(Trait) is a universal getter. It accepts a Trait enum, which it
	 * then uses to get the appropriate private variable representing the
	 * requested stat modifier requested. This cuts down on the number of
	 * methods required to get all instance variables.
	 * 
	 * @param stat
	 *            - the Trait Enum for the desired stat, AGILITY, INTELLECT,
	 *            etc.
	 * @return And int that represents the value of the requested stat.
	 */
	public int getMod(Trait stat) {
		if (stat == Trait.AGILITY)
			return agilityMod;
		else if (stat == Trait.INTELLECT)
			return intellectMod;
		else if (stat == Trait.STRENGTH)
			return strengthMod;
		else if (stat == Trait.TECHNIQUE)
			return techniqueMod;
		else if (stat == Trait.TOUGHNESS)
			return toughnessMod;
		else
			return 0;
	}

	/**
	 * toString() will return a string that will be a description of the gear.
	 * This will be used when a player looks at an item or else where.
	 * 
	 * @return A String description of the gear
	 */
	public String toString() {
		return super.toString();
	}

	/**
	 * The compareTo(Gear) method compares the Gear's name to the passed in
	 * Gear's name.
	 */
	@Override
	public int compareTo(Gear gear) {
		return this.getName().compareTo(((Gear) gear).getName());
	}

	/**
	 * This method returns the default behavior the the gear.
	 * 
	 * @param movable
	 *            - The player calling the default behavior.
	 * @return - A String that represents the text representation of the default
	 *         behavior or action.
	 */
	public abstract void getDefaultBehavior(Movable movable);

}
