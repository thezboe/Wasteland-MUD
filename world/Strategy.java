package world;

import java.io.Serializable;

/**
 * Strategy is used by MOB's to set there default behavior. Some MOBs will have an
 * aggressive strategy that will attack human player's, others will simple greet players
 * as they enter a room.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 *
 */
public abstract class Strategy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * attackBehavior will be used for aggressive MOB's.  If a player enters a room this class
	 * will be called to determine if the MOB will attack the player or not.
	 * 
	 * @param me The MOB using the strategy
	 * @param enemy The potential enemy
	 */
	public abstract void attackBehavior(Mobile me, Movable enemy);
	
	/**
	 * reactToSend will be used whenever a player enters the room a mobile is currently in. If
	 * the aggressive calls for the MOB to attack, it will tell the player he/she is about to be
	 * attacked. If the strategy is set up to simply greet the MOB will greet.
	 * 
	 * @param sent The player name will be used from the string sent
	 * @param mob The MOB sending the text to the player
	 */
	public abstract void reactToSend(String sent, Mobile mob);

	/**
	 * onRoomChange is used for MOB's who move around in the world. Some MOB's will not move and
	 * stay stationary. This will method will be used for when a MOB moves to a room, and based
	 * on there strategy will either attack a human player, say something, etc.
	 */
	public abstract void onRoomChange();

}
