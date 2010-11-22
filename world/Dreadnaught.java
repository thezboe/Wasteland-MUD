package world;

/**
 * Dreadnaught is a character class which relies on melee weapons and a high
 * strength score to kill enemies. it has the ability to wound enemies so that
 * they cannot dish out as much damage. It extends CharacterClass.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class Dreadnaught extends CharacterClass {

	private static final long serialVersionUID = 1L;
	private static Dreadnaught instance = new Dreadnaught();

	/**
	 * Dreadnaught's constructor defines the trait modifier integers which are
	 * applied to the characters when the class is loaded to the player.
	 * 
	 */
	private Dreadnaught() {
		this.setMod(3, Trait.STRENGTH);
		this.setMod(1, Trait.AGILITY);
		this.setMod(4, Trait.TOUGHNESS);
		this.setMod(0, Trait.INTELLECT);

	}

	/**
	 * Dreadnaught is a singleton, since it is only called when classes are set.
	 * This method returns the static instance.
	 * 
	 * @return The instance variable, the sole instance of Dreadnaught
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
	public void Wound(Player player, Movable target) {
		if (player.getStat(Trait.TECHNIQUE) > 5) {
			player.sendToPlayer("You strike " + target.getName()
					+ " and wound them!");
			player.setStat(player.getStat(Trait.TECHNIQUE)-5,Trait.TECHNIQUE);
			player.attack(target);
			CombatManager attack = new CombatManager(player, target);
		}

	}

	@Override
	public String toString() {
		return "Dreadnaught";
	}

}
