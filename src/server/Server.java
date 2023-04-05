package server;

/**
 * The Server class function is to create a server socket
 * and accept clients in order to form a baseline for communication
 * 
 *  
 * @author	Albi Zhaku
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import sql.SQLConnector;

public class Server {

	@SuppressWarnings("unused")
	private int port; // port number for the server
	private ServerSocket socket; // socket object for the server
	private static SQLConnector sql = new SQLConnector("test", "root", "");;
	private List<ClientListener> clients; // a list of ClientListeners
	private List<String> onlineUsers = new ArrayList<>(); // a list of ClientListeners

	/**
	 * The constructor used to create the port
	 * 
	 * @param port - the port number to be used for the server
	 */
	public Server(int port) {
		this.port = port;// Instantiates the port
		clients = new ArrayList<>();// Instantiates the list
		try {
			this.socket = new ServerSocket(port); // Instantiates the socket field with a new ServerSocket object
			connectionLoop(); // calls the connectionLoop method

			System.out.println("Server is running..."); // Relays a server running message to console

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Broadcasts the list of online users to all connected clients and sends a
	 * message notifying that a user has connected.
	 * 
	 * @param onlineUsers - a list of online users
	 */
	public void broadcastList(List<String> onlineUsers) {

		for (ClientListener cl : clients) {
			try {
				cl.getOutput().writeObject(onlineUsers);
				cl.getOutput().flush();
				cl.getOutput().reset();

				cl.getOutput().writeObject(cl.getUsername() + " has connected!");
				cl.getOutput().flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Broadcasts a message to all clients notifying them of a user's disconnection.
	 * 
	 * @param username - the username of the user who disconnected
	 */
	public void broadcastDisconnect(String username) {
		for (ClientListener client : clients) {
			try {
				client.getOutput().writeObject(username + " has disconnected!");
				client.getOutput().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Accepts all clients in the Server thread
	 */
	public void connectionLoop() {
		try {
			while (!socket.isClosed()) {
				Socket client = socket.accept(); // Accepts user

				System.out.println("Client has connected"); // User accepted message

				ClientListener cl = new ClientListener(client, this); // Creates a client listener for each clients
				Thread.sleep(100);

				if (onlineUsers.contains(cl.getUsername())) { // Checks if user is currently online
					cl.getReadThread().interrupt();
					cl.closeSocket();
					cl.setDuplicate(true);
				} else {
					clients.add(cl); // Adds client to clients list
					onlineUsers.add(cl.getUsername());
					Thread.sleep(100);
					broadcastList(onlineUsers);
				}

			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static SQLConnector getSql() {
		return sql;
	}

	/**
	 * The main class to run an instance of the Server
	 */
	public static void main(String[] args) {
		new Server(1234);

	}

	/**
	 * Returns a list of currently active users.
	 * 
	 * @return List of Strings
	 */
	public List<String> getActiveUsers() {
		return onlineUsers;
	}

	/**
	 * Returns a list of connected clients.
	 * 
	 * @return List of ClientListeners
	 */
	public List<ClientListener> getClients() {
		return this.clients;
	}

}
