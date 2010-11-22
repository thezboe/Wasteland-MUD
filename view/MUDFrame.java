package view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import model.*;

/**
 * MUDFrame is contains the main visual display for the user. It will contain a
 * main combat window that will be used for playing in the game. There is a chat
 * tab that will be used to show only the chat between players. It will send all
 * entered text to the server and based on what command was used the proper
 * command will be called. The text will be displayed using ANSI coloring to
 * help distinguish between items, exits and so on.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrie
 * 
 */
@SuppressWarnings("serial")
public class MUDFrame extends JFrame {

	private CombatPanel comPanel;
	private ChatPanel chPanel;
	private JMenuBar menuBar;
	private JMenu mainFile;
	private JMenuItem exitMenuItem;
	private JMenuItem aboutMenuItem;
	private network.Client client;

	/**
	 * MUDFrame constructor is empty.
	 */
	public MUDFrame() {
	}

	/**
	 * The main method that will start the MUD. It will create a new MUDFrame
	 * and ask for a IP and port to connect to the server.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MUDFrame frame = new MUDFrame();
		if (frame.init() != true) {
			JOptionPane.showMessageDialog(null, "Couldn't connect to server");
			System.exit(1);
		}
	}

	/*
	 * This method initializes the mud frame components.
	 */
	private boolean init() {

		this.client = new network.Client("150.135.1.17", 4000);
		//this.client = new network.Client("localhost", 4000);

		if ((this.client != null) && this.client.connect()) {
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
			this.setTitle("MUD");
			this.setPreferredSize(new Dimension(1250, 700));
			this.setLocation(25, 50);
			this.setBackground(Color.LIGHT_GRAY);
			this.setLayout(null);

			menuBar = new JMenuBar();
			mainFile = new JMenu("File");
			exitMenuItem = new JMenuItem("Exit");
			aboutMenuItem = new JMenuItem("About");
			mainFile.add(aboutMenuItem);
			mainFile.add(exitMenuItem);
			exitMenuItem.addActionListener(new ExitListener());
			aboutMenuItem.addActionListener(new AboutListener());
			menuBar.add(mainFile);
			this.setJMenuBar(menuBar); 
           
			// Construct chat panel text field and button
			JTextField textField = new JTextField();
			textField.addKeyListener(new EnterListener());
			textField.setSize(new Dimension(1100, 18));
			textField.setLocation(new Point(15, 600));

			JButton enterBut = new JButton("Enter");
			enterBut.addActionListener(new ButtonListener());
			enterBut.setSize(new Dimension(70, 30));
			enterBut.setLocation(new Point(1155, 593));
			comPanel = new CombatPanel(textField);
			chPanel = new ChatPanel(textField);

			this.add(enterBut);
			this.add(textField);

			JLabel comLabel = new JLabel("Combat Window");
			comLabel.setSize(new Dimension(265, 30));
			comLabel.setLocation(new Point(275, 15));
			comLabel.setFont(new Font("OCR A Extended", Font.BOLD, 20));
			comLabel.setForeground(Color.RED);

			this.add(comLabel);

			JLabel chLabel = new JLabel("Chat Window");
			chLabel.setSize(new Dimension(225, 30));
			chLabel.setLocation(new Point(950, 15));
			chLabel.setFont(new Font("OCR A Extended", Font.BOLD, 12));
			chLabel.setForeground(Color.RED);
			this.add(chLabel);

			comPanel.setSize(new Dimension(775, 525));
			chPanel.setSize(new Dimension(400, 525));

			comPanel.setLocation(new Point(15, 50));
			chPanel.setLocation(new Point(830, 50));

			this.add(comPanel);
			this.add(chPanel);

			MudModel.getMudModel().addObserver(comPanel);
			MudModel.getMudModel().addObserver(chPanel);

			this.setResizable(false);

			this.pack();
			this.setVisible(true);

			textField.requestFocusInWindow();

			this.client.start();
			return true;
		} else
			return false;
	}

	/*
	 * AboutListener the the ActionListener the the About menu item. it will
	 * display the programmers names and a short description of the program.
	 */
	private class AboutListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String about = "Waste Land" 
							 + "\n\n Authors: Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrie";
			JOptionPane.showMessageDialog(null, about);
		}

	}

	/*
	 * ExitListener is the ActionListener for the Exit menu item. It will simple
	 * call System.exit(0) which will exit out of the current program.
	 */
	private class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}

	/*
	 * This method sends text commands to the network.client.
	 */
	private void sendCommand() {
		String temp = comPanel.getTextField().getText();
		client.sendCommand(temp);
		comPanel.getTextField().setText("");
		if (temp.equalsIgnoreCase("quit")) {
			System.exit(0);
		}
	}

	/*
	 * ButtonListener is the main ActionListener for the Enter button on the
	 * combat panel. Even though hitting enter will work to enter a command the
	 * button will serve the same purpose as hitting the enter key. It will
	 * clear the field once the button is pressed and reset the focus to the
	 * field.
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			sendCommand();
		}
	}

	/*
	 * EnterListener is the KeyListener that waits for the Enter key to be
	 * pressed. This Listener will perform the exact same operation as the
	 * ButtonListener for the Enter button on the panel. It will take the
	 * command and execute it, clear the field and reset the focus back to the
	 * field.
	 */
	private class EnterListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				sendCommand();
			}
		}

		public void keyReleased(KeyEvent e) {

		}

	}
}
