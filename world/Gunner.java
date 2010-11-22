package world;

/**
 * Gunner is a character class which describes a character who excels at range
 * combat, but relies largely on the damage of long range weapons to succeed. It
 * extends CharacterClass.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class Gunner extends CharacterClass {

	private static final long serialVersionUID = 1L;
	private static Gunner instance = new Gunner();

	/**
	 * Gunner's constructor defines the trait modifier integers which are
	 * applied to the characters when the class is loaded to the player.
	 * 
	 */
	private Gunner() {
		this.setMod(0, Trait.STRENGTH);
		this.setMod(4, Trait.AGILITY);
		this.setMod(1, Trait.TOUGHNESS);
		this.setMod(3, Trait.INTELLECT);

	}

	/**
	 * Gunner is a singleton, since it is only called when classes are set. This
	 * method returns the static instance.
	 * 
	 * @return The instance variable, the sole instance of Gunner
	 */
	public static CharacterClass getInstance() {
		return instance;
	}

	/**
	 * Snipe is a command which can be used to start combat. it allows a Gunner
	 * to hit a target once before combat begins, using range to their
	 * advantage.
	 * 
	 * @param player
	 *            The player using the Snipe attack.
	 * @param target
	 *            The player's target to shoot at.
	 */
	public void Snipe(Player player, Movable target) {
		if (player.getStat(Trait.TECHNIQUE) > 5) {
			player.sendToPlayer("You line up a shot, and attack "
					+ target.getName() + " at range!");
			player.setStat(player.getStat(Trait.TECHNIQUE)-5,Trait.TECHNIQUE);
			player.attack(target);
			CombatManager attack = new CombatManager(player, target);
		}
	}

	@Override
	public String toString() {
		return "Gunner";
	}

}
