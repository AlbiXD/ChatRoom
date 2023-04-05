/**
 * The ClientListener class function is to handle clients 
 * disconnects, messages and broadcasting messages.
 *  
 * @author	Albi Zhaku
 */

package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientListener implements Runnable {

	private ObjectInputStream in; // BufferedReader for clients
	private Socket client; // Socket for the client
	private ObjectOutputStream out; // PrintWriter for clients
	public String username; // The username of the client
	private Thread readThread; // The thread to read incoming messages from the client
	private Server server; // The instance of the Server class that the client is connected to
	private boolean isDuplicate = false; // A flag to indicate whether the current client is a duplicate

	/**
	 * Constructor for the ClientListener class.
	 * 
	 * @param client - the socket for the client
	 * 
	 * @param server - the server object
	 */
	public ClientListener(Socket client, Server server) {
		this.client = client;
		this.server = server;
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			in = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		readThread = new Thread(this);
		readThread.start();

	}

	@Override
	/**
	 * This method reads messages from the client and broadcasts them to other
	 * clients.
	 * 
	 * @throws SocketException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void run() {
		while (!client.isClosed()) {
			Object obj;

			try {
				obj = in.readObject();

				if (obj instanceof String) {
					if (username == null) {
						this.username = obj.toString();
					} else {

						String message = obj.toString();
						broadcastMessage(message, client);
						System.out.println(message);
					}
				}

			} catch (SocketException e) {
				if (!isDuplicate) {
					server.getActiveUsers().remove(username);
					server.getClients().remove(this);
					server.broadcastList(server.getActiveUsers());
					server.broadcastDisconnect(this.username);
				}

				break;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Returns the socket object for the client.
	 * 
	 * @return Socket
	 */
	public Socket getClient() {
		return client;
	}

	/**
	 * Sets the socket object for the client.
	 * 
	 * @param client - the socket object to be set
	 */
	public void setClient(Socket client) {
		this.client = client;
	}

	/**
	 * Returns the output stream object for the client.
	 * 
	 * @return ObjectOutputStream
	 */
	public ObjectOutputStream getOutput() {
		return out;
	}

	/**
	 * Returns the username associated with the client.
	 * 
	 * @return String
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Returns the thread object responsible for reading messages from the client.
	 * 
	 * @return Thread
	 */
	public Thread getReadThread() {
		return readThread;
	}

	/**
	 * Sends a message to all clients except for the sender.
	 * 
	 * @param message - the message to send
	 * 
	 * @param client  - the client who sent the message
	 */
	public void broadcastMessage(String message, Socket client) {
		for (ClientListener cl : server.getClients()) {
			if (!(cl.getClient().equals(client))) {
				try {
					cl.getOutput().writeObject(message);
					cl.getOutput().flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Closes the socket for the client.
	 */
	public void closeSocket() {
		try {
			this.in.close();
			this.out.close();
			this.client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns whether or not the client is a duplicate.
	 * 
	 * @return boolean
	 */
	public boolean isDuplicate() {
		return isDuplicate;
	}

	/**
	 * Sets whether or not the client is a duplicate.
	 * 
	 * @param isDuplicate - the value to set the isDuplicate field to
	 */
	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

}
