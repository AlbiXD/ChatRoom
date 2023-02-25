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

	private int port; // port number for the server
	private ServerSocket socket; // socket object for the server
	private static SQLConnector sql;
	public static List<ClientListener> clients; // a list of ClientListeners

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
			this.sql = new SQLConnector("Gghjfk337@");
			System.out.println("Server is running..."); // Relays a server running message to console
		} catch (IOException e) {
			e.printStackTrace();
		}

		connectionLoop(); // calls the connectionLoop method

	}

	/**
	 * Accepts all clients in the Server thread
	 */
	public void connectionLoop() {
		try {
			while (!socket.isClosed()) {
				Socket client = socket.accept(); // Accepts user

				System.out.println("Client has connected"); // User accepted message

				ClientListener cl = new ClientListener(client); // Creates a client listener for each clients
				clients.add(cl); // Adds client to clients list

			}
		} catch (IOException e) {
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

}
