package model;

/**
 * The ChatCommand class currently is only being used to help the gui determine
 * what panel to update. If the text being passed from the server is a chat
 * command, then the model.MudModel will create a ChatCommand object that wraps
 * the text to pass to the gui and pass it when it notifies observers.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class ChatCommand {

	private String text;

	/**
	 * This constructor creates a ChatCommand object which wraps the text to
	 * pass to the gui.
	 * 
	 * @param text
	 */
	public ChatCommand(String text) {
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
