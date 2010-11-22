package model;

/**
 * The CombatCommand class currently is only being used to help the gui
 * determine what panel to update. If the text being passed from the server
 * originated as a combat command, then the model.MudModel will create a
 * CombatCommand object that wraps the text to pass to the gui and passes it
 * when it notifies observers.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 */
public class CombatCommand {
	private String text;

	/**
	 * This constructor creates a CombatCommand object which wraps the text to
	 * pass to the gui.
	 * 
	 * @param text
	 */
	public CombatCommand(String text) {
		this.text = text;
	}

	/**
	 * This method returns the text stored in the object.
	 * 
	 * @return - A String that represents the text stored in the object.
	 */
	public String getText() {
		return this.text;
	}

}
