package world;

import java.util.Scanner;

/**
 * The Greets class is a simple strategy that waves to players as they enter or
 * leave rooms. It extends Strategy
 * 
 *@author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class Greets extends Strategy {

	private static final long serialVersionUID = 1L;

	@Override
	public void onRoomChange() {
		// Does nothing when it changes rooms. Shouldn't be changing rooms
		// anyway.

	}

	@Override
	public void reactToSend(String sent, Mobile mob) {
		Scanner sentStuff = new Scanner(sent);
		String aPlayer = sentStuff.next().trim();
		String nextWord = sentStuff.next().trim();
		if (nextWord.equalsIgnoreCase("enters")) {
			if (World.getInstance().playerExists(aPlayer)) {
				((Room) mob.getLocation()).sendToRoom(mob.getName()
						+ " waves to " + aPlayer + ".");
			}
		} else if (nextWord.equalsIgnoreCase("exits")){
			if (World.getInstance().playerExists(aPlayer)) {
				((Room) mob.getLocation()).sendToRoom(mob.getName()
						+ " waves goodbye to " + aPlayer + ".");
			}
		}

	}

	@Override
	public void attackBehavior(Mobile me, Movable enemy) {
		((Room) me.getLocation()).sendToRoom(me.getName()
				+ " shouts, \" Help! " + enemy.getName()
				+ " is attacking me! \" ");

	}

}
