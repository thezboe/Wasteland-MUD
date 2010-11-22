package world;

import java.io.IOException;

/**
 * This WorldNotFoundException class extends NullPointException and can be used
 * in place of NullPointerExceptions when it applies to a variable that
 * represents the entire databaseArray in world.World.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class WorldNotFoundException extends IOException {

	private static final long serialVersionUID = 1L;

	/**
	 * This default constructor sends "World is null, World file(s) not created, or World file not found."
	 * to the super classes constructor.
	 */
	public WorldNotFoundException() {
		super("World is null, check databaseArray file in MudWorld folder to see if exists or has errors.");
	}
}
