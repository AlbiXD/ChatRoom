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
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientListener implements Runnable {

	private BufferedReader in; // BufferedReader for clients
	private Socket client; // Socket for the client
	private PrintWriter out; // PrintWriter for clients

	public ClientListener(Socket client) {
		this.client = client;
		try {
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	// This thread reads messages from client and broadcasts to others
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

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public PrintWriter getOutput() {
		return out;
	}

	/**
	 * 
	 */
	public void broadcastMessage(String message, Socket client) {
		for (ClientListener cl : Server.clients) {
			if (!(cl.getClient().equals(client))) {
				cl.getOutput().println(message);

			}
		}

	}

}
