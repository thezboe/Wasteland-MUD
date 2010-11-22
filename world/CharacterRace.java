package world;

/**
 * Not implemented, future iteration.
 * 
 * CharacterRace will be the main container for the different races that
 * are available to play as in the game.  It will hold default values for 
 * each race when the player first starts to play.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrie
 *
 */
public class CharacterRace {
	private int agilityMod;
	private int strengthMod;
	private int toughnessMod;
	private int intellectMod;
	private int techniqueMod;
	
	/**
	 * CharacterRace constructor will not take in any parameters. The 
	 * default values for all the traits will be hard coded in.
	 */
	public CharacterRace() {
		
	}
	
	/**
	 * getMod is a universal getter. It accepts a Trait enum, which it then uses
	 * to get the appropriate private variable representing the requested stat
	 * modifier requested. This cuts down on the number of methods required to
	 * get all instance variables.
	 * 
	 * @return int The value of the requested stat modifier.
	 */
	public int getMod(Trait stat) {
		return 0;

	}
}
