package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private Socket socket; // socket to connect to
	private int port; // port number for the server
	private PrintWriter out; // output stream for the server
	private BufferedReader in; // input stream for the server
	private Main main;

	/**
	 * A constructor for the client
	 * 
	 * @param port - the port number for the server
	 */
	public Client(int port) {
		this.port = port;
		try {
			this.socket = new Socket("localhost", this.port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		Thread thread = new Thread(this);
		thread.start();

	}

	/**
	 * Requests and sends message to the server
	 */
	public void sendMessage(String message) {
		out.println(message);

	}

	@Override
	/**
	 * Reads incoming messages from the server
	 */
	public void run() {

		while (true) {
			try {
				main.setText(in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void setMain(Main main) {
		this.main = main;
	}

	public Main getMain() {
		// TODO Auto-generated method stub
		return this.main;
	}

}