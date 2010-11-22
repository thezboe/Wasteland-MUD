package world;

import java.io.Serializable;

/**
 * CharacterClass is the super method for all of the different classes that can
 * be played in the game. Each different class will hold different starting
 * values for all the traits. The traits will be able to be modified once the
 * player levels up in game.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public abstract class CharacterClass implements Serializable {

	private static final long serialVersionUID = 1L;
	private int agilityMod;
	private int strengthMod;
	private int toughnessMod;
	private int intellectMod;

	// private int tecniqueMod;

	/**
	 * CharacterClass constructor is an empty constructor.
	 */
	public CharacterClass() {

	}

	/**
	 * getMod is a universal getter. It accepts a Trait enum, which it then uses
	 * to get the appropriate private variable representing the requested stat
	 * modifier. This cuts down on the number of methods required to get all
	 * instance variables.
	 * 
	 * @return int The value of the requested stat modifier.
	 */
	public int getMod(Trait stat) {
		if (stat == Trait.INTELLECT)
			return intellectMod;
		else if (stat == Trait.STRENGTH)
			return strengthMod;
		else if (stat == Trait.AGILITY)
			return agilityMod;
		else if (stat == Trait.TOUGHNESS)
			;
		return toughnessMod;

	}

	/**
	 * setMod is a universal setter. It accepts a Trait enum, which it then uses
	 * to set the appropriate private variable representing the requested stat
	 * modifier. This cuts down on the number of methods required to set all
	 * instance variables.
	 * 
	 * @param int value The value to set the stat to.
	 * @param Trait
	 *            stat The stat to modify.
	 */
	public void setMod(int value, Trait stat) {
		if (stat == Trait.INTELLECT)
			intellectMod = value;
		else if (stat == Trait.STRENGTH)
			strengthMod = value;
		else if (stat == Trait.AGILITY)
			agilityMod = value;
		else if (stat == Trait.TOUGHNESS)
			toughnessMod = value;
	}

	/**
	 * This method overrides Object's toString() to display a simple text
	 * message of the player's class.
	 * 
	 * @return A String that represents the player's class.
	 */
	public abstract String toString();
}
