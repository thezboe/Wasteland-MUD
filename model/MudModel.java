package model;

import java.util.Observable;
import server.*;
import world.*;

/**
 * MudModel is a singleton. It is used for the two panels, CombatPanel and
 * ChatPanel, to observe. When a command is sent through the GUI this model will
 * be used to send the action. This action will either be a game action(attack,
 * look..) or a chat command that will either send it to all players or one
 * player. This class will eventually be used when we implement more advanced
 * features, like allowing users to save or print their game text and chat text.
 * It receives text from the network.Client when the network.Client receives a
 * message.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class MudModel extends Observable {

	private static MudModel model = new MudModel();
	private String textModel;
	private String chatTextModel;

	// This private constructor along with the private static MudModel variable
	// and public static MudModle method allows MudModel to exist as a singleton
	private MudModel() {
		textModel = "";
		chatTextModel = "";
	}

	/**
	 * addText method will add text to the end of the text model string and
	 * notify observers.
	 * 
	 * @param text
	 *            String to add to end of text
	 */
	public void addText(String text) {
		textModel += text;
		setChanged();
		this.notifyObservers(new CombatCommand(text));
	}

	/**
	 * addChatText method will add text to the end of the chat text model string
	 * and notifies observers.
	 * 
	 * @param text
	 *            String to add to end of text
	 */
	public void addChatText(String text) {
		chatTextModel += text;
		setChanged();
		this.notifyObservers(new ChatCommand(text));
	}

	/**
	 * This method returns the instance of the MudModel.
	 * 
	 * @return - The MudModel instance.
	 */
	public static MudModel getMudModel() {
		return model;
	}
}
