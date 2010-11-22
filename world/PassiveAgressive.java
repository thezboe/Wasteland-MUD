package world;

import java.util.Scanner;



/**
 * PassiveAggressive will wait a little to attack a player, wait for them to
 * leave or stay. If the player stays in the room, the mob will eventually
 * attack. upon entering the room. It extends Strategy and depends on CombatManager to start the
 * attack.
 * 
 *@author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class PassiveAgressive extends Strategy {

	private static final long serialVersionUID = 1L;
	
	private Player toAttack;
	private Mobile mySelf;

	@Override
	public void onRoomChange() {
		
	}

	@Override
	public void reactToSend(String sent, Mobile mob) {
		Scanner sentStuff = new Scanner(sent);
		String aPlayer = sentStuff.next().trim();
		if (sentStuff.next().trim().equalsIgnoreCase("enters")){
			if (World.getInstance().playerExists(aPlayer)
					&& World.getInstance().playerIsLoggedOn(aPlayer)) {
				((Room) mob.getLocation()).sendToRoom(mob.getName()
						+ " see's you, but doesn't appear to be threatening.");
			}
		}
	}

	@Override
	public void attackBehavior(Mobile me, Movable enemy) {
		me.resolveAttack(enemy);

	}

}
