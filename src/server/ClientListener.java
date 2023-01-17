package server;

/**
 * The ClientListener class essentially creates a whole separate thread
 * for the Server to receive and send messages.
 * 
 * @author	Albi Zhaku
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientListener implements Runnable {

	private BufferedReader in; // BufferedReader for clients
	private Socket client; // Socket for the client
	private PrintWriter out; // PrintWriter for clients

	/**
	 * The constructor to assign a Client Listener to each client
	 * 
	 * @param client - The client we are trying to listen to
	 */
	public ClientListener(Socket client) {
		this.client = client;
		try {
			out = new PrintWriter(client.getOutputStream(), true); // Gets the output stream for the client
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); // Gets the input stream for the
																						// client
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread thread = new Thread(this); // Creates a separate thread for receiving and sending messages
		thread.start();
	}

	@Override
	/**
	 * This thread is responsible for receiving incoming messages and sending the
	 * message to other clients in the server
	 */
	public void run() {
		while (true) {
			try {
				// Receives message
				String msg = in.readLine();
				System.out.println(msg);
				broadcastMessage(msg, client);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Gets the client that we are currently listening to
	 * 
	 * @return Socket client - returns the client that is being listened
	 */
	public Socket getClient() {
		return client;
	}

	/**
	 * Sets the client field to the client we specify
	 * 
	 * @param Socket client - the client we are setting
	 */
	public void setClient(Socket client) {
		this.client = client;
	}

	/**
	 * Returns the output stream of a client
	 * 
	 * @return PrintWriter out - returns the output stream for the user
	 */
	public PrintWriter getOutput() {
		return out;
	}

	/**
	 * This method broadcasts a users message to other users
	 * 
	 * @param String message - Gets the message that the client sent
	 * @param Socket client - Gets the client that sends the message
	 */
	public void broadcastMessage(String message, Socket client) {
		for (ClientListener cl : Server.clients) {
			if (!(cl.getClient().equals(client))) {
				cl.getOutput().println(message);
			}
		}

	}

}
