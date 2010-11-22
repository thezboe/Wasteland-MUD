package world;

import java.util.Random;
import java.util.Scanner;

/**
 * Mutters extends Strategy. It is a simple strategy that only sends chatter
 * text on a regular interval to the room. The mutter will send "looks at" text
 * to room on reactsToSend. It will yell for help if being attacked. It also
 * implements Runnable.
 * 
 *@author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class Mutters extends Strategy implements Runnable {

	private static final long serialVersionUID = 1L;
	private String chatterText;
	private transient Thread thread;
	private Mobile mobile;

	/**
	 * This Mutters constructor takes in a String to set its chatter text. It
	 * also starts it thread to repeat the text every so often.
	 * 
	 * @param chatterText
	 *            - The String to set as the chatter text.
	 */
	public Mutters(String chatterText) {
		this.chatterText = chatterText;
		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public void attackBehavior(Mobile me, Movable enemy) {
		((Room) me.getLocation()).sendToRoom(me.getName()
				+ " shouts, \" Help! " + enemy.getName()
				+ " is attacking me! \" ");
	}

	@Override
	public void onRoomChange() {
		// TODO not impplemented yet.

	}

	/*
	 * This method will send a mobile "looks at " message to the room.
	 * 
	 * @param sent - String that was sent to mobile.
	 * 
	 * @param mob - Mobile that will send the looks at message.
	 */
	@Override
	public void reactToSend(String sent, Mobile mob) {
		Scanner sentStuff = new Scanner(sent);
		String aPlayer = sentStuff.next().trim();
		if (World.getInstance().playerExists(aPlayer)) {
			((Room) mob.getLocation()).sendToRoom("" + mob.getName()
					+ " looks at " + aPlayer + ".");
		}
	}

	/**
	 * This method sets the conversation text of the Mutter.
	 * 
	 * @param text
	 *            - The text the Mutter will send to others as conversation or
	 *            chatter.
	 */
	public void setChatterText(String chatterText) {
		this.chatterText = chatterText;
	}

	/**
	 * This method sets the mobile for the Mutter.
	 */
	public void setMobile(Mobile mobile) {
		this.mobile = mobile;
	}

	/**
	 * This run() method runs every 20 seconds and repeats the chatter text of
	 * the mutter to the room. It then leaves the room if its random number
	 * equals an exit that exits in the room.
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

				if (this.mobile != null) {

					Room currentRoom = (Room) mobile.getLocation();
					if (currentRoom == null) {
						System.out.println("Mutter room not sent.");
					}

					((Room) mobile.getLocation()).sendToRoom(chatterText);

					boolean moved = false;
					while (!moved) {
						Random timeToMove = new Random();
						int moveNum = timeToMove.nextInt(4);

						switch (moveNum) {
						case 0:
							if (((Room) this.mobile.getLocation())
									.getExitDestination(Direction.NORTH) != null) {

								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " exits North.");
								this.mobile.moveToRoom(((Room) this.mobile
										.getLocation())
										.getExitDestination(Direction.NORTH));
								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " arrives from the South.");
								moved = true;
							}
							break;
						case 1:
							if (((Room) this.mobile.getLocation())
									.getExitDestination(Direction.SOUTH) != null) {

								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " exits South.");
								this.mobile.moveToRoom(((Room) this.mobile
										.getLocation())
										.getExitDestination(Direction.SOUTH));
								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " arrives from the North.");
								moved = true;
							}
							break;
						case 2:
							if (((Room) this.mobile.getLocation())
									.getExitDestination(Direction.EAST) != null) {

								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " exits East.");
								this.mobile.moveToRoom(((Room) this.mobile
										.getLocation())
										.getExitDestination(Direction.EAST));
								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " arrives from the West.");
								moved = true;
							}
							break;
						case 3:
							if (((Room) this.mobile.getLocation())
									.getExitDestination(Direction.WEST) != null) {

								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " exits West.");
								this.mobile.moveToRoom(((Room) this.mobile
										.getLocation())
										.getExitDestination(Direction.WEST));
								((Room) this.mobile.getLocation())
										.sendToRoom(this.mobile.getName()
												+ " arrives from the East.");
								moved = true;
							}
							break;
						default:
							break;
						}
					}
				}
				Thread.sleep(20000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
