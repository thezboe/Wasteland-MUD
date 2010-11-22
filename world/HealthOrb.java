package world;

/**
 * The HealthOrb class can be used to create objects a player can consume to
 * increase their hit points. On creation, the health orb will store the max
 * health points available to a player. When a player uses the health orb, the
 * health orb will increase the player's hit points by the amount of health
 * points it has available and set itself to zero health points. A thread in the
 * health orb runs every 30 seconds to regenerate the HealthOrb's health points
 * by one increment until it reaches its max health points. It extends Gear and
 * Implements Runnable.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class HealthOrb extends Gear implements Runnable {

	private static final long serialVersionUID = 1L;
	private int healthPoints;
	private int maxHealthPoints;
	private transient Thread thread;

	/**
	 * The HealthOrb constructor takes in a String name, a String description,
	 * and the max health points the HealthOrb will have. The healthPoints
	 * available to the player will be start out at the max health points and
	 * remain until used by a player.
	 * 
	 * @param name
	 *            - A String that represents the name of the health orb.
	 * @param description
	 *            - A String that represents the description of the health orb.
	 * @param healthPoints
	 *            - A int that represents the initial and max health points of
	 *            the health orb.
	 */
	public HealthOrb(String name, String description, int healthPoints) {
		super(name, description);
		this.healthPoints = healthPoints;
		this.maxHealthPoints = healthPoints;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void getDefaultBehavior(Movable movable) {
		if (this.healthPoints == 0) {
			movable.sendToPlayer("Orb is recharging.");
		}
		movable.setStat(healthPoints + movable.getStat(Trait.HITPOINTS),
				Trait.HITPOINTS);
		movable.sendToPlayer("Your hit points are "
				+ movable.getStat(Trait.HITPOINTS));
		this.healthPoints = 0;
	}

	/**
	 * This run() method will run every 10 seconds and increment the health
	 * orb's health points by one until its health points reaches its max health
	 * points.
	 */
	@Override
	public void run() {
		try {
			while (true) {

				synchronized (World.getInstance().getLockObject()) {
					while (World.getInstance().threadsLocked()) {
						try {
							World.getInstance().getLockObject().wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				if (this.healthPoints < this.maxHealthPoints) {
					this.healthPoints++;
				}

				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
