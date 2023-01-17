package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

	private Socket socket; // socket to connect to
	private int port; // port number for the server
	private PrintWriter out; // output stream for the server
	private String name; // name for the client
	private Scanner scan; // scanner class
	private BufferedReader in; // input stream for the server

	/**
	 * A constructor for the client
	 * 
	 * @param port - the port number for the server
	 */
	public Client(int port) {
		this.port = port;
		this.scan = new Scanner(System.in);

		try {
			this.socket = new Socket("localhost", this.port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Enter a username");
		name = scan.nextLine();

		Thread thread = new Thread(this);
		thread.start();

		requestMessage();

	}

	/**
	 * Requests and sends message to the server
	 */
	public void requestMessage() {
		while (!socket.isClosed()) {
			out.println(name + ": " + scan.nextLine());
		}

	}

	/*
	 * Main method for the Client class
	 */
	public static void main(String[] args) {
		new Client(1234);
	}

	@Override
	/**
	 * Reads incoming messages from the server
	 */
	public void run() {

		while (true) {
			try {
				System.out.println(in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
