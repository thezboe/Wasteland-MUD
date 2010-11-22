package server;

import java.io.Serializable;

/**
 * The Communication class is used by network.Client and server.Client to pass
 * text objects encapsulated within an object back and forth.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class Communication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;

	/**
	 * The Communication constructor takes a String and stores it as text it can
	 * use to pass a long to a client or server.
	 * 
	 * @param text
	 *            - The String that will be stored and passed along later.
	 */
	public Communication(String text) {
		this.text = text.trim();
	}

	/**
	 * The getText() method retrieves the text stored in the Communication
	 * object.
	 * 
	 * @return - A String of text stored in the Communication object.
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * The setText(String) method sets the text that will be stored in the
	 * Communication object.
	 * 
	 * @param text
	 *            - The text to set the internal text object to.
	 */
	public void setText(String text) {
		this.text = text.trim();
	}

}
