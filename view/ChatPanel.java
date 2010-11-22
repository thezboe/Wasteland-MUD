package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.*;

/**
 * ChatPanel is the main panel that will allow players to talk freely to one
 * another. It contains its own TextField and TextArea to allow for easier
 * chatting. It observes model.MudWorld and is contained in the MudFrame.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrie
 * 
 */
public class ChatPanel extends JPanel implements Observer {
	private JTextField textField;
	private JTextArea chatText;
	private JScrollPane allTextPane;
	private final static String newline = "\n";

	/**
	 * ChatPanel constructor will initiate all the swing components. The set up
	 * is very similar to the CombatPanel. It will have a JScrollPane that has a
	 * JTextArea contained with in it. It will display all the chatting that is
	 * going on in the server between the players.
	 * 
	 * @param field
	 *            - A JTextField to display the text.
	 */
	public ChatPanel(JTextField field) {
		this.setLayout(null);
		this.setTextField(field);

		chatText = new JTextArea();
		chatText.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
		chatText.setEditable(false);
		chatText.setLineWrap(true);
		chatText.setWrapStyleWord(true);
		chatText.setSize(new Dimension(550, 525));
		chatText.setBackground(Color.BLACK);
		allTextPane = new JScrollPane(chatText);
		allTextPane.setSize(new Dimension(400, 525));
		allTextPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		allTextPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		for (int i = 0; i < 28; i++) {
			chatText.append("\n");
		}

		allTextPane.setBackground(Color.BLACK);
		this.add(allTextPane);

	}

	/**
	 * update is used whenever there needs to be text printed to the console. It
	 * will either update the chat or the combat window based on what the
	 * command that was called was.
	 */
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof model.ChatCommand) {
			String temp = ((model.ChatCommand) arg1).getText();
			chatText.setForeground(Color.WHITE);
			chatText.append(temp + newline);
			chatText.setCaretPosition(chatText.getText().length() - 1);
			repaint();
		}
	}

	/**
	 * The method sets the JTextfield textField instance variable of the class.
	 */
	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	/**
	 * This method returns the JTextField textField instance variable of the
	 * class.
	 * 
	 * @return - The JTextField textField instance variable of the class.
	 */
	public JTextField getTextField() {
		return this.textField;
	}
}
