package world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * CombatManager is called to control any attacks that might be initialized
 * during game play.  It has two movable's that will be the two players that are
 * involved in the fight. It also creates two times, one for each player. These timers
 * will act on there own timer listeners and as long as each player is still alive.
 * Both threads will be stopped once one of the players dies.  
 *
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */

public class CombatManager {
	
	private AttackTimerListener attackTimerListener = new AttackTimerListener();
	//private Attack2TimerListener attack2TimerListener = new Attack2TimerListener();

	private Timer attackTimer;
	private Timer attackTimer2;

	private Movable attackerOne;
	private Movable attackerTwo;
	
	/**
	 * A CombatManager will take in two movable's in its constructor and set them to
	 * the instance variables attackerOne and attackerTwo.  It will also initiate both
	 * of the timer's to start will a one second delay. Then the timers will be started
	 * and the attacking will begin.
	 * 
	 * @param attacker1 Player that started the attack
	 * @param attacker2 Player that is being attacked
	 */
	public CombatManager(Movable attacker1, Movable attacker2) {
	
		this.attackerOne = attacker1;
		this.attackerTwo = attacker2;
		((Room)this.attackerOne.getLocation()).sendToRoom(attackerOne.getName() + " has entered combat");
		this.attackerOne.setFighting(true);
		if (attackerTwo instanceof Player) {
			this.attackerTwo.setFighting(true);
		}
		attackTimer = new Timer(1000, attackTimerListener);
		//attackTimer2 = new Timer(1000, attack2TimerListener);

		attackerOne.sendToPlayer("An attack agianst, " + attacker2.getName() + ", has begun!");
		
		attackTimer.start();
		//attackTimer2.start();

	}
	
	/**
	 * isFighting is a simple boolean method that is used to check to make sure that
	 * both players still have health and can keep fighting. If one of the players
	 * is dead it will stop both timers.
	 * 
	 * @return True if both players are still alive, false otherwise
	 */
	public boolean isFighting() {
		attackTimer.setDelay(5000);
		//attackTimer2.setDelay(5000);
		
		if (!World.getInstance().playerIsLoggedOn(attackerOne.getName())){
			attackTimer.stop();
			//attackTimer2.stop();
			this.attackerOne.setFighting(false);
			this.attackerTwo.setFighting(false);
			return false;
		}
		if (attackerTwo instanceof Player) {
			if (!World.getInstance().playerIsLoggedOn(attackerTwo.getName())){
				attackTimer.stop();
				//attackTimer2.stop();
				this.attackerOne.setFighting(false);
				this.attackerTwo.setFighting(false);
				return false;

			}
		}
		
		if (attackerOne.getStat(Trait.HITPOINTS) <= 0){
			attackerOne.sendToPlayer("You have perished");
			attackTimer.stop();
			//attackTimer2.stop();
			this.attackerOne.setFighting(false);
			this.attackerTwo.setFighting(false);
			if (attackerOne instanceof Player){
				((Player)attackerOne).respawn();

			}else {
				((Mobile)attackerOne).waitForRespawn();
			}
			return false;
		}
		if (attackerTwo.getStat(Trait.HITPOINTS) <= 0){
			attackerOne.sendToPlayer("You have killed " + attackerTwo.getName() + "!");
			attackTimer.stop();
			//attackTimer2.stop();
			this.attackerOne.setFighting(false);
			this.attackerTwo.setFighting(false);
			((Room)attackerTwo.getLocation()).remove(attackerTwo.getName());
			attackerTwo.moveToRoom((Room)World.getInstance().getDatabaseObject(0));
			if (attackerTwo instanceof Player){
				((Player)attackerTwo).respawn();

			}else {
				((Mobile)attackerTwo).waitForRespawn();
			}
			return false;
		}
		
		if(attackerOne.getRoomId() != attackerTwo.getRoomId()){
			attackTimer.stop();
			//attackTimer2.stop();
			this.attackerOne.setFighting(false);
			this.attackerTwo.setFighting(false);
			return false;
		}
		
		if(attackerOne.getRoomId() != attackerTwo.getRoomId()){
			attackTimer.stop();
			//attackTimer2.stop();
			this.attackerOne.setFighting(false);
			this.attackerTwo.setFighting(false);
			return false;
		}
		return true;
	}
	
	/**
	 * AttackTimerListener will be the ActionListener for the timer for the player
	 * that started the attack.  It will call isFighting() before attacking the
	 * opposing player.
	 *
	 */
	private class AttackTimerListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			if (isFighting()){
				attackerOne.attack(attackerTwo);
				attackerTwo.attack(attackerOne);

			}
		}
		
	}
	
	/**
	 * Attack2TimerListener will be the ActionListener for the timer for the player
	 * that is being attacked.  It will call isFighting() before attacking the
	 * opposing player.
	 *
	 */
//	private class Attack2TimerListener implements ActionListener {
//
//		public void actionPerformed(ActionEvent arg0) {
//			
//			if (isFighting()){
//				attackerTwo.attack(attackerOne);
//			}
//		}
//		
//	}
}
