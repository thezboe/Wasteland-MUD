package world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.Timer;

/**
 * Aggressive is a particularly nasty mob strategy. It will attack any player
 * upon entering the room. Variations of Aggressive could be built which
 * discriminate between targets based on level, or to even follow players into
 * other rooms. It extends Strategy and depends on CombatManager to start the
 * attack.
 * 
 *@author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class Agressive extends Strategy {

	private static final long serialVersionUID = 1L;
	private Player toAttack;
	private Mobile mySelf;

	/*
	 * Aggressive mobiles do nothing special when they attack.
	 */
	@Override
	public void attackBehavior(Mobile me, Movable enemy) {
		me.resolveAttack(enemy);

	}

	/*
	 * No special actions on a room change.
	 */
	@Override
	public void onRoomChange() {
		// TODO Auto-generated method stub

	}

	/*
	 * Upon seeing a message in the room, an Aggressive mob will attempt to
	 * attack whatever player created the message.
	 */
	/*
	 * Upon seeing a message in the room, an Aggressive mob will attempt to
	 * attack whatever player created the message.
	 */
	@Override
	public void reactToSend(String sent, Mobile mob) {
		Scanner sentStuff = new Scanner(sent);
		String aPlayer = sentStuff.next().trim();
		if (sentStuff.next().trim().equalsIgnoreCase("enters")) {
			if (World.getInstance().playerExists(aPlayer)
					&& World.getInstance().playerIsLoggedOn(aPlayer)) {

				mySelf = mob;
				Room room = (Room) World.getInstance().getDatabaseObject(
						mySelf.getRoomId());
				room.remove(mySelf.getName());
				room.add(mySelf);
				mySelf.setLocation(room);

				((Room) mySelf.getLocation()).refreshPlayers();
				((Room) mySelf.getLocation()).refreshMobiles();

				toAttack = World.getInstance().getPlayer(aPlayer);
				((Room) mob.getLocation()).sendToRoom(mob.getName()
						+ " moves into attack position..");
				Timer attackTimer = new Timer(10000, new AttackTimerListener());
				attackTimer.setRepeats(false);
				attackTimer.start();
			}
		}
	}

	private class AttackTimerListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			System.out.println(mySelf + " " + toAttack + " "
					+ mySelf.getRoomId() + " " + toAttack.getRoomId());
			if (mySelf == null || toAttack == null) {

			} else if (mySelf.getRoomId() == toAttack.getRoomId()) {
				CombatManager attackStart = new CombatManager(toAttack, mySelf);
			}

		}

	}

}