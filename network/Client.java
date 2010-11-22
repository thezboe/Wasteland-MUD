package network;

import java.io.*;
import java.net.*;
import server.Communication;
import model.*;

/**
 * network.Client is the main class that communicates with the server.MudServer.
 * Once it gets a socket back from the server, it will create the output and the
 * input streams for putting text in the gui and getting info from the server.
 * It also has methods that will be able to send info to the server and receive
 * any other messages that the server may of sent out. It depends on MudModel to
 * send the received text to.
 * 
 * @author Matt Turner, Ross Bottorf, Zach Boe, Jonathan Perrine
 * 
 */
public class Client implements Runnable {

	private Thread thread;
	private Socket socket;
	private String hostName;
	private int port;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;

	/**
	 * Client constructor will take in two different parameters. One will be the
	 * string that represents the host name and an int value that will be the
	 * port for the server. These two values will allow the Client to attempt to
	 * access the server.
	 * 
	 * @param host
	 *            The name of the host
	 * @param port
	 *            The port number of the server
	 */
	public Client(String host, int port) {
		this.hostName = host;
		this.port = port;
		this.socket = null;
		this.thread = new Thread(this);
	}

	/**
	 * The connect method is how the Client attempts to communicate with the
	 * server.MudServer. It will create a new socket with the host and port that
	 * was read in from the constructor. It will return true if it was able to
	 * connect and false otherwise.
	 * 
	 * @return True if connect was successful and false otherwise.
	 */
	public boolean connect() {

		try {
			socket = new Socket(this.hostName, this.port);

			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException ioe2) {
					System.out.println("Could not close connection.");
					System.out.println(ioe2.getMessage());
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * sendCommand(String) is the main method for how a message gets to the
	 * server. It will take the parameter string and create a Communication
	 * object with it. Then it will serialize the Communication object and send
	 * it to the server.
	 * 
	 * @param output
	 *            String to send to the server.
	 * @return True always
	 */
	public String sendCommand(String output) {
		Communication communication = new Communication(output);
		try {
			outputStream.writeObject(communication);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	/**
	 * The start() method allows the JFrame to tell the Client when to start its
	 * thread.
	 */
	public void start() {
		this.thread.start();
	}

	/**
	 * run() is the main method for the network.Client thread. It will look for
	 * messages being sent from the server.MudServer to the network.Client. If
	 * it receives a message, it will send the text to the model.MudWorld
	 */
	public void run() {

		while (true) {
			try {
				this.inputStream.read();
				try {
					String temp = ((Communication) this.inputStream
							.readObject()).getText();
					if (temp != null
							&& temp.toLowerCase().indexOf("chat ") == 0) {
						temp = temp.substring(5);
						MudModel.getMudModel().addChatText(temp + "\n");
					} else {
						MudModel.getMudModel().addText(temp + "\n");
					}
				} catch (IOException e) {
					MudModel.getMudModel().addText("Disconnected from server");
					break;
				} catch (ClassNotFoundException e) {
					MudModel.getMudModel().addText("Disconnected from server");
					break;
				}
			} catch (IOException e1) {
				MudModel.getMudModel().addText("Disconnected from server");
				break;
			}
		}
	}

}
