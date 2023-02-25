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
	private String username;
	private String email;
	private boolean logedIn = false;

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
	// Reads messages from client and broadcasts to others
	public void run() {
		while (true) {
			while (!logedIn) {
				out.println("Press 1 to Log-In\nPress 2 to Sign-Up");
				String msg = null;

				try {
					msg = in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (msg.equals("1")) {
					String email = null;
					String password = null;

					try {
						out.println("Enter Email");
						email = in.readLine();

						out.println("Enter Password");
						password = in.readLine();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if ((Server.getSql().getUser().logIn(email, password) == true)) {
						out.println("Log In Successful");
						logedIn = true;
						this.email = email;
						try {
							ResultSet rs = Server.getSql().getConnection().createStatement()
									.executeQuery("SELECT username FROM albi.user where email = '" + email + "'");

							while (rs.next()) {
								this.username = rs.getString("username");
							}

						} catch (SQLException e) {
							e.printStackTrace();
						}
						break;
					} else {
						out.println("Log-In Failed");
					}
				}
				if (msg.equals("2")) {
					try {
						out.println("Enter Username: ");
						String username = in.readLine();

						out.println("Enter Email: ");
						String email = in.readLine();

						out.println("Enter Password: ");
						String password = in.readLine();

						Server.getSql().getUser().signUp(email, username, password, out);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			try {
				// Receives message
				String msg = in.readLine();
				System.out.println(this.username + ": " + msg);
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
				cl.getOutput().println(this.username + ": " + message);
			}
		}

	}

}
