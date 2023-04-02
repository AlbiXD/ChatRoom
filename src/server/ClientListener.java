package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientListener implements Runnable {

	private ObjectInputStream in; // BufferedReader for clients
	private Socket client; // Socket for the client
	private ObjectOutputStream out; // PrintWriter for clients
	public String username;

	public ClientListener(Socket client) {
		this.client = client;
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
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
			Object obj;

			try {
				obj = in.readObject();

				if (obj instanceof String) {
					if (username == null) {
						this.username = obj.toString();
						System.out.println(username);
					} else {

						String message = obj.toString();
						broadcastMessage(message, client);
						System.out.println(message);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
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

	public ObjectOutputStream getOutput() {
		return out;
	}

	public String getUsername() {
		return this.username;
	}

	/**
	 * 
	 */
	public void broadcastMessage(String message, Socket client) {
		for (ClientListener cl : Server.clients) {
			if (!(cl.getClient().equals(client))) {
				try {
				cl.getOutput().writeObject(message);
				cl.getOutput().flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}

	public void broadcastList(List<String> onlineUsers, String connectedUser) {

		for (ClientListener cl : Server.clients) {
			try {
				cl.getOutput().writeObject(onlineUsers);
				cl.getOutput().flush();
				cl.getOutput().reset();


				cl.getOutput().writeObject(connectedUser + " has connected!");
				cl.getOutput().flush();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
