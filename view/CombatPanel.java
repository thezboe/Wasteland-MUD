package view;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

/**
 * CombatPanel will be the main panel that will control the text that is
 * displayed to the console during the game. It contains a JTextArea that will
 * allow the user the interact with the game world. It has a null layout which
 * allows for the individual components to be added where I see fit. It observes
 * model.MudWorld and is contained in the MudFrame.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrie
 * 
 */
public class CombatPanel extends JPanel implements Observer {
	private JTextArea worldText;
	private JScrollPane allTextPane;
	private JTextField textField;
	private final static String newline = "\n";

	/**
	 * CombatPanel constructor will initiate all of the swing components. It
	 * will set a size of each and set the location relative to where it needs
	 * to be positioned on the panel. The null layout allows for the location to
	 * be set. The panel's color will be set to black to give the illusion of a
	 * real console.
	 */
	public CombatPanel(JTextField field) {
		this.setLayout(null);
		this.setTextField(field);
		worldText = new JTextArea();
		worldText.setFont(new Font("Lucida Console", Font.PLAIN, 16));
		worldText.setEditable(false);
		worldText.setLineWrap(true);
		worldText.setWrapStyleWord(true);
		worldText.setSize(new Dimension(550, 550));
		worldText.setBackground(Color.BLACK);
		allTextPane = new JScrollPane(worldText);
		allTextPane.setSize(new Dimension(775, 525));
		allTextPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		allTextPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		for (int i = 0; i < 29; i++) {
			worldText.append("\n");
		}
		allTextPane.setBackground(Color.BLACK);

		worldText.setCaretPosition(worldText.getText().length());
		this.add(allTextPane);
	}

	/**
	 * update is used whenever there needs to be text printed to the console. It
	 * will either update the chat or the combat window based on what the
	 * command that was called was.
	 */
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof model.CombatCommand) {
			String temp = ((model.CombatCommand) arg1).getText();
			worldText.setForeground(Color.WHITE);
			worldText.append(temp + newline);
			worldText.setCaretPosition(worldText.getText().length() - 1);
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
