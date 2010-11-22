package world;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.Timer;

/**
 * Mobile is a class which represents Mobiles, or any non-player object with
 * behavior similar to that of a player (such as the ability to fight, or a set
 * of stats.) Like a player, it has combat stats, implements the Movable
 * interface and extends DatabaseObject.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class Mobile extends DatabaseObject implements Movable {

	private static final long serialVersionUID = 1L;
	private Room startingLoc;
	private transient boolean isFighting;
	private int hitPoints;
	private int maxHitPoints;
	private int technique;
	private int maxTechnique;
	private int agility;
	private int strength;
	private int toughness;
	private int intellect;
	private GearList gearList;
	private Strategy myStrategy;
	private int roomId;
	private Mobile mySelf;

	/**
	 * A Mobile needs only a String when constructed (which becomes the Mobile's
	 * name) as default values are placed for other relevant information.
	 * Ideally, a Mobile should be further customized using the various setters.
	 * The constructor does create some default values for the mobiles stats.
	 * 
	 * It is worth noting that Mobile and Player stats appear identical, but can
	 * be different. Players rely on armor and toughness - Mobs only use
	 * toughness to reduce damage, so this might be higher. Similarly, strength
	 * might be higher to account for a lack of weapons.
	 * 
	 * 
	 * @param name
	 *            A String that represents the name of the new Mobile.
	 * 
	 */
	public Mobile(String name) {
		super(name);
		myStrategy = new Greets();
		this.gearList = new GearContainer(name + "'s gear", name + "'s gear:",
				20, false);
		this.agility = 5;
		this.hitPoints = 30;
		this.intellect = 5;
		this.strength = 4;
		this.technique = 4;
		this.toughness = 4;
		this.maxHitPoints = 30;
		this.maxTechnique = 4;
		this.gearList.setLocation(this);
		startingLoc = null;
		mySelf = this;
		this.isFighting = false;
	}

	@Override
	public synchronized void moveToRoom(Room destination) {

		if (this.getLocation() != null) {
			((Room) this.getLocation()).remove(this.getName());
		}

		destination.add(this);
		this.roomId = destination.getDatabaseRef();
		this.setLocation(destination);
		sendToPlayer(((Room) destination).generateDescription());
	}

	@Override
	public void use(String itemName) {
		this.gearList.getGear(itemName).getDefaultBehavior(this);
	}

	@Override
	public void setStat(int value, Trait stat) {
		if (stat == Trait.AGILITY)
			agility = value;
		else if (stat == Trait.HITPOINTS)
			hitPoints = value;
		else if (stat == Trait.INTELLECT)
			intellect = value;
		else if (stat == Trait.MAXHITPOINTS) {
			maxHitPoints = value;
			
		} else if (stat == Trait.MAXTECHNIQUE) {
			maxTechnique = value;
			technique = maxTechnique;
		} else if (stat == Trait.STRENGTH)
			strength = value;
		else if (stat == Trait.TECHNIQUE)
			technique = value;
		else
			toughness = value;
	}

	@Override
	public int getStat(Trait stat) {
		if (stat == Trait.AGILITY)
			return agility;
		else if (stat == Trait.HITPOINTS){
			if (hitPoints < 0) {
				return 0;
			}
			return hitPoints;
		}else if (stat == Trait.INTELLECT)
			return intellect;
		else if (stat == Trait.MAXHITPOINTS)
			return maxHitPoints;
		else if (stat == Trait.MAXTECHNIQUE)
			return maxTechnique;
		else if (stat == Trait.STRENGTH)
			return strength;
		else if (stat == Trait.TECHNIQUE)
			return technique;
		else
			return toughness;

	}

	@Override
	public void sendToPlayer(String message) {
		if (myStrategy != null)
			myStrategy.reactToSend(message, this);
	}

	@Override
	public void attack(Movable enemy) {
		myStrategy.attackBehavior(this, enemy);
	}

	@Override
	public boolean addGear(Gear item) {
		return this.addGear(this, item);
	}

	@Override
	public boolean addGear(Movable movableToNotify, Gear gear) {
		return this.gearList.addGear(this, gear);
	}

	@Override
	public boolean giveGear(Movable movableToNotify, String itemName,
			String otherName) {
		return this.gearList.giveGear(movableToNotify, itemName, otherName);
	}

	@Override
	public Gear getGear(String itemName) {
		return this.gearList.getGear(itemName);
	}

	@Override
	public boolean canBeCarried() {
		return this.gearList.canBeCarried();
	}

	@Override
	public int getMaxGearCount() {
		return this.gearList.getMaxGearCount();
	}

	@Override
	public void setMaxGearCount(int max) {
		this.gearList.setMaxGearCount(max);

	}

	@Override
	public String inspect() {
		return this.gearList.inspect();
	}

	@Override
	public void dropGear(String itemName, Room room, Movable movableToNotify) {
		this.gearList.dropGear(itemName, room, this);
	}

	public int getRoomId() {
		return this.roomId;
	}

	@Override
	public List<Gear> listGear() {
		return this.gearList.listGear();
	}

	@Override
	/*
	 * The toString method overrides Object's toString method. This String is
	 * the way that the Mobile will be seen in the room, and uses the name
	 * inherited from Database Object.
	 * 
	 * @return String of text from the room that the Mobile sees.
	 */
	public String toString() {
		return this.getName() + " (DB:" + this.getDatabaseRef() + ")";
	}

	/**
	 * resolveAttack runs calculations to determine whether or not the Mobile
	 * hits an opponent in combat. This is done in two stages - An attack roll
	 * and a damage roll.
	 * 
	 * @param enemy
	 *            - The Movable to attack.
	 */
	public void resolveAttack(Movable enemy) {

		// Attack Roll
		int attackRoll = (int) (Math.random() * 10) + this.agility
				- enemy.getStat(Trait.AGILITY);
		if (attackRoll < 3) {
			this.sendToPlayer("You miss by " + (5 - attackRoll));
			enemy.sendToPlayer(this.getName() + " misses you.");
		}

		// Damage Roll
		else {
			int damage = Math.max(1, this.strength
					- (((Player) enemy).getStat(Trait.TOUGHNESS)/3)
					- ((Player) enemy).getArmor().getDamageReduction());
			int newHP = enemy.getStat(Trait.HITPOINTS) - damage;
			enemy.setStat(newHP, Trait.HITPOINTS);
			enemy.sendToPlayer(this.getName() + " damages you for " + damage
					+ " hitpoints!");
		}

	}

	/**
	 * This method sets the strategy of the Mobile. A Strategy is used to
	 * determine how the mobile interacts with the world.
	 * 
	 * @param myStrategy
	 *            - A Strategy object used to determine the mobile's behavior.
	 */
	public void setStrategy(Strategy myStrategy) {
		this.myStrategy = myStrategy;
	}

	/**
	 * This method returns the strategy of the mobile.
	 * 
	 * @return - The mobile's Strategy objectg used to determine how it behaves
	 *         with the world.
	 */
	public Strategy getStrategy() {
		return myStrategy;
	}

	/**
	 * waitForRespawn is called whenever a player(s) kill a MOB. It will start a
	 * timer that will wait ten seconds then it will call a deadTimerListener.
	 * The listener will move the MOB back to wherever it was spawned when the
	 * server started. It will also reset the hp back to its maximum.
	 */
	public void waitForRespawn() {
		((Room) mySelf.getLocation()).refreshPlayers();
		((Room) mySelf.getLocation()).refreshMobiles();
		Timer deadTimer = new Timer(10000, new DeadTimerListener());
		deadTimer.setRepeats(false);
		deadTimer.start();
	}

	/**
	 * DeadTimerListener is the action listener for the timer that is created in
	 * the waitForRespawn method. It will call all the required classes that are
	 * needed to place a MOB back into the world and for setting there HP.
	 */
	private class DeadTimerListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			startingLoc.add((Mobile) mySelf);
			mySelf.moveToRoom(startingLoc);
			mySelf.setStat(maxHitPoints, Trait.HITPOINTS);
			((Room) mySelf.getLocation()).refreshPlayers();
			((Room) mySelf.getLocation()).refreshMobiles();
			((Room)mySelf.getLocation()).sendToRoom(mySelf.getName() + " has respawned");
		}
	}

	/**
	 * setStart is called once whenever a MOB is created for the first time. It
	 * will set a Room variable to the first location the MOB is set at. This
	 * will be used for repawning MOBs if they die.
	 * 
	 * @param startLoc
	 *            Room MOB is first placed
	 */
	public void setStart(Room startLoc) {
		this.startingLoc = startLoc;
	}

	/**
	 * Not used, MOB's can be attacked by mulitple players
	 */
	public void setFighting(boolean fighting) {
		// Not used, MOBs can be attacked by multiple players
	}

	/**
	 * Not used, MOB's can be attacked by mulitple players
	 */
	public boolean getFighting() {
		return false;
	}
}
