/**
 * The Client class function is to connect to the server and communicate
 * with the server back and forth.
 * 
 * @author	Albi Zhaku
 */




package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {

	private Socket socket; // socket to connect to
	private int port; // port number for the server
	private ObjectOutputStream out; // output stream for the server
	private ObjectInputStream in; // input stream for the server
	private ChatClient main;
	private String username;
	public static List<String> activeUsers;

	/**
	 * A constructor for the client
	 * 
	 * @param port - the port number for the server
	 */
	public Client(int port, String username) {
		this.port = port;
		this.username = username;
		try {
			this.socket = new Socket("localhost", this.port);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			// Sends over the username to the server
			try {
				out.writeObject(username);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		Thread thread = new Thread(this);
		thread.start();

	}

	/**
	 * Sends message to the server
	 */
	public void sendMessage(String message) {

		if (message.substring(0, 1).equals(" ")) {
			return;
		}

		try {
			out.writeObject(this.username + ": " + message);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	/**
	 * Reads incoming messages from the server
	 */
	public void run() {

		while (!socket.isClosed()) {
			Object obj;

			try {

				obj = in.readObject();
				if (obj instanceof String) {

					String message = obj.toString();
					main.setText(message);
				}

				else if (obj instanceof List) {
					activeUsers = (ArrayList<String>) obj;
					main.getActiveUserTextArea().setText("");
					for (String users : activeUsers) {
						main.getActiveUserTextArea().setText(main.getActiveUserTextArea().getText() + users + "\n");

					}
				}

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("User already is connected");
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Sets the instance of the ChatClient main to the current instance.
	 * 
	 * @param main - the ChatClient instance to set as main.
	 */
	public void setMain(ChatClient main) {
		this.main = main;
	}

	/**
	 * Returns the ChatClient instance that is currently set as main.
	 * 
	 * @return ChatClient instance
	 */
	public ChatClient getMain() {
		return this.main;
	}

	/**
	 * Returns the socket object for this Client instance.
	 * 
	 * @return Socket object
	 */
	public Socket getSocket() {
		return this.socket;
	}

}