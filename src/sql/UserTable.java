/**
 * UserTable class is responsible for checking if the user can log-in
 * if the user exists, and makes it so the user can sign-up
 * 
 * @author	Albi Zhaku
 */

package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTable {

	private Statement stmnt;
	private Connection connection;
	private SQLConnector sql;

	private static UserTable instance = null;

	/**
	 * Constructor for the UserTable class that initializes the connection to the
	 * database and creates the user table if it doesn't exist.
	 * 
	 * @param connection - the connection to the database
	 * @param sql        - the SQLConnector object
	 */
	private UserTable(Connection connection, SQLConnector sql) {
		this.connection = connection;
		this.sql = sql;

		try {
			this.stmnt = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		createTable();
	}

	/**
	 * Creates a table for users in order to store their usernames, passwords, and
	 * emails.
	 */
	public void createTable() {

		try {
			stmnt.execute("CREATE TABLE IF NOT EXISTS " + sql.getDatabase()
					+ ".user(user_id varchar(30) primary key, username varchar(12), email varchar(100), password varchar(30));");
			System.out.println("User Table Created");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sign-Up Method to add the user in the database.
	 * 
	 * @param email    - the email of the user
	 * @param username - the username of the user
	 * @param password - the password of the user
	 * 
	 * @return boolean - returns true if sign up is successful, else false
	 */
	public boolean signUp(String email, String username, String password) {
		ResultSet rs;

		// Check if username is less than 6 or greater than 12
		if (username.length() < 6 || username.length() > 12) {
			System.out.println("Invalid Amount of Characters");
			return false;
		}

		// Check if username is greater than 6 characters and less than 12 characters
		if (password.length() < 6) {
			System.out.println("Invalid Amount of Characters");
			return false;
		}

		try {
			// Check if email is taken
			rs = stmnt.executeQuery("Select email, username from " + sql.getDatabase() + ".user");
			while (rs.next()) {
				if (email.equals(rs.getString("email"))) {
					System.out.println("Email already exists");
					return false;
				}
				if (username.equals(rs.getString("username"))) {
					System.out.println("Username already exists");
					return false;
				}
			}

			// Generate user id
			ResultSet rs3 = stmnt.executeQuery("Select count(user_id) as count from " + sql.getDatabase() + ".user");
			rs3.next();

			String user_id = "U" + rs3.getInt("count");

			PreparedStatement stmnt3 = connection.prepareStatement(
					"INSERT INTO " + sql.getDatabase() + ".user(user_id, username, email, password) VALUES(?,?,?,?);");

			stmnt3.setString(1, user_id);
			stmnt3.setString(2, username);
			stmnt3.setString(3, email);
			stmnt3.setString(4, password);

			stmnt3.execute();

			System.out.println("Sign Up Successful");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * Log-In method to log in the user to the network.
	 * 
	 * @param username - the username of the user
	 * @param password - the password of the user
	 * @return boolean - returns true if login
	 */
	public boolean logIn(String username, String password) {
		// check if email exists
		ResultSet rs;
		try {
			rs = this.stmnt.executeQuery("SELECT username, password FROM " + sql.getDatabase() + ".user");

			while (rs.next()) {
				if (rs.getString("username").equals(username)) {
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
	public static UserTable getInstance(Connection connection, SQLConnector sql) {
		if (instance == null) {
			instance = new UserTable(connection, sql);
		}

		return instance;
	}

}
