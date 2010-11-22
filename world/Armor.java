package world;

/**
 * Armor is a type of item which protects players and mobiles. In general, it
 * lowers the amount of damage that a creature or player takes from an attack.
 * It extends Gear.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class Armor extends Gear {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int damageReduction;
	private ArmorType type;

	/*
	 * Armor type represents the gauge of the armor, light, medium, heavy
	 * (L,M,H).
	 */
	private enum ArmorType {
		L, M, H
	}

	/**
	 * Primary constructor. Creates default light, level 1 armor.
	 * 
	 * @param name
	 *            The name of the Armor
	 * @param description
	 *            A description of the Armor.
	 */
	public Armor(String name, String description) {
		super(name, description);
		this.setItemLevel(1);
		this.setArmorType('L');
		setDamageReduction(this.getItemLevel() * 1);
	}

	/**
	 * A more distinct constructor, which allows the armor's level and type to
	 * be specified.
	 * 
	 * @param name
	 *            - String name of the armor.
	 * @param description
	 *            - String that represents
	 * @param level
	 *            - An int that represents how much protection the armor offers.
	 * @param type
	 *            - A char that represents the ArmorType type: light, medium, or
	 *            heavy (L,M,H).
	 */
	public Armor(String name, String description, int level, char typeChar) {
		super(name, description);
		this.setArmorType(typeChar);
		this.setItemLevel(level);
		if (type == ArmorType.L)
			setDamageReduction(this.getItemLevel() * 1);
		if (type == ArmorType.M)
			setDamageReduction(this.getItemLevel() * 2);
		if (type == ArmorType.H)
			setDamageReduction(this.getItemLevel() * 3);
	}

	/**
	 * The setArmorType(char) method sets an armor's type to light, medium, or
	 * heavy (L,M,H)
	 * 
	 * @param T
	 *            - a char that represents the type of the armor: light, medium,
	 *            or heavy (L,M,H)
	 * @return - true if the type was set, false otherwise.
	 */
	public boolean setArmorType(char T) {

		if (T == 'L') {
			type = ArmorType.L;
			return true;
		} else if (T == 'M') {
			type = ArmorType.M;
			return true;
		} else if (T == 'H') {
			type = ArmorType.H;
			return true;
		} else
			return false;
	}

	@Override
	public void getDefaultBehavior(Movable movable) {
		((Player) movable).setArmor(this);

	}

	/**
	 * Setter for damageReduction - Should rarely be used, as damageReduction
	 * should be calculated automatically.
	 * 
	 * @param damageReduction
	 *            integer which becomes new DamageReduction.
	 */
	private void setDamageReduction(int damageReduction) {
		this.damageReduction = damageReduction;
	}

	/**
	 * Getter for damageReduction armor stat.
	 * 
	 * @return The Armor's current Damage Reduction.
	 */
	public int getDamageReduction() {
		return damageReduction;
	}

}
