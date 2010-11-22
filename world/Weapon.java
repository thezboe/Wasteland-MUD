package world;

/**
 * Weapon is a sub-class of gear, and will represent any weapon that exists in
 * the world. Each weapon will be given a damage modifier that will increase the
 * damage a player will inflict upon a MOB.
 * 
 *@author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class Weapon extends Gear {

	private static final long serialVersionUID = 1L;
	private int damage;

	/**
	 * The Weapon constructor takes in a String name and a String description
	 * and sets the default damage of the weapon to 2.
	 * 
	 * @param name
	 *            The name of the weapon
	 * @param description
	 *            The description of the weapon
	 */
	public Weapon(String name, String description) {
		super(name, description);
		this.setDamage(2);

	}

	/**
	 * Weapon will take in 4 parameters. A name for the weapon, a description of
	 * the weapon, a level modifier, and the damage modifier. The name and the
	 * description will be passed to the super class, Gear, to be stored there.
	 * The level and damage modifier, because unique to the weapon, will be
	 * stored internally.
	 * 
	 * @param name
	 *            A String that represents the name of the weapon
	 * @param description
	 *            A String that represents the description of the weapon
	 * @param level
	 *            An int that represents the level modifier of the weapon
	 * @param damage
	 *            An int that represents the damage modifier of the weapon
	 */
	public Weapon(String name, String description, int level, int damage) {
		super(name, description);
		this.setItemLevel(level);
		this.setDamage(damage);
	}

	/**
	 * setDamage will take in a integer value. This integer value will be set to
	 * the damage modifier for the weapon.
	 * 
	 * @param dam
	 *            An int that represents the Damage modifier
	 */
	public void setDamage(int dam) {
		damage = dam;
	}

	/**
	 * getDamage will return the damage modifier for the weapon
	 * 
	 * @return An int that represents the damage modifier for the weapon
	 */
	public int getDamage() {
		return damage;
	}

	@Override
	public void getDefaultBehavior(Movable movable) {
		((Player) movable).setWeapon(this);

	}
}
