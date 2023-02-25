package sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTable {

	private Statement stmnt;
	private Connection connection;

	private static UserTable instance = null;

	private UserTable(Connection connection) {
		this.connection = connection;

		try {
			this.stmnt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		createTable();
	}

	/**
	 * Creates a table for users in order to store their usernames passwords and
	 * emails
	 */
	public void createTable() {

		try {
			stmnt.execute(
					"CREATE TABLE IF NOT EXISTS User(user_id varchar(30) primary key, username varchar(12), email varchar(100), password varchar(30));");
			System.out.println("User Table Created");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sign-Up Method to add the user in the database
	 */
	public boolean signUp(String email, String username, String password, PrintWriter out) {
		ResultSet rs;

		// Check if username is less than 6 or greater than 12
		if (username.length() < 6 || username.length() > 12) {
			out.println("Invalid Amount of Characters");
			return false;
		}

		// Check if username is greater than 6 characters and less than 12 characters
		if (password.length() < 6) {
			out.println("Invalid Amount of Characters");
			return false;
		}

		try {
			// Check if email is taken
			rs = stmnt.executeQuery("Select email, username from albi.user");
			while (rs.next()) {
				if (email.equals(rs.getString("email"))) {
					out.println("Email already exists");
					return false;
				}
				if (username.equals(rs.getString("username"))) {
					out.println("Username already exists");
					return false;
				}
			}

			// Generate user id
			ResultSet rs3 = stmnt.executeQuery("Select count(user_id) as count from albi.user");
			rs3.next();

			String user_id = "U" + rs3.getInt("count");

			PreparedStatement stmnt3 = connection
					.prepareStatement("INSERT INTO albi.user(user_id, username, email, password) VALUES(?,?,?,?);");

			stmnt3.setString(1, user_id);
			stmnt3.setString(2, username);
			stmnt3.setString(3, email);
			stmnt3.setString(4, password);

			stmnt3.execute();

			out.println("Sign Up Successful");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Log-In method to log in the user to the network
	 */
	public boolean logIn(String email, String password) {
		// check if email exists
		ResultSet rs;
		try {
			rs = this.stmnt.executeQuery("SELECT email, password FROM albi.user");

			while (rs.next()) {
				if (rs.getString("email").equals(email)) {
					if (rs.getString("password").equals(password)) {
						return true;
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Singleton Method in order to not create multiple instances of the User Table
	 */
	public static UserTable getInstance(Connection connection) {
		if (instance == null) {
			instance = new UserTable(connection);
		}

		return instance;
	}

}
